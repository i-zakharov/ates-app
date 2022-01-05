package ru.zim.ates.tasktracker.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zim.ates.common.application.exception.AppException;
import ru.zim.ates.common.messaging.config.AppEventType;
import ru.zim.ates.common.messaging.producer.ProducerNotifyEvent;
import ru.zim.ates.common.messaging.schemaregistry.EventEnvelope;
import ru.zim.ates.common.messaging.schemaregistry.EventType;
import ru.zim.ates.common.standartimpl.consumer.user.model.AppUser;
import ru.zim.ates.common.standartimpl.consumer.user.repository.AppUserRepository;
import ru.zim.ates.tasktracker.dto.TaskCreateRequestDto;
import ru.zim.ates.tasktracker.mapper.TaskMapper;
import ru.zim.ates.tasktracker.model.Task;
import ru.zim.ates.tasktracker.model.TaskStatus;
import ru.zim.ates.tasktracker.repository.AssigneeRepository;
import ru.zim.ates.tasktracker.repository.TaskRepository;

import static ru.zim.ates.common.messaging.config.MqConfig.ATES_TASK_TRACKER_PRODUCER_NAME;

@Service
@Slf4j
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private AppUserRepository userRepository;
    @Autowired
    private AssigneeRepository assigneeRepository;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Task getById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new AppException(String.format("Task with id = %d not found.")));
        return task;
    }

    @Transactional
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Transactional
    public Task create(TaskCreateRequestDto dto) {
        Task task = taskMapper.fromCreateDto(dto, userRepository);
        taskRepository.save(task);

        EventEnvelope eventEnvelope = EventEnvelope.preSetBuilder()
                .eventType(AppEventType.ATES_TASK_CREATED)
                .producer(ATES_TASK_TRACKER_PRODUCER_NAME)
                .eventVersion("1")
                .data(taskMapper.toResponceDto(task)).build();
        applicationEventPublisher.publishEvent(new ProducerNotifyEvent(this, eventEnvelope));

        Map<String, Object> data = new HashMap<>();
        data.put("publicId", task.getPublicId());
        EventEnvelope taskPendingEvent = EventEnvelope.preSetBuilder()
                .eventType(AppEventType.ATES_TASK_PENDING)
                .producer(ATES_TASK_TRACKER_PRODUCER_NAME)
                .eventVersion("1")
                .data(data).build();
        applicationEventPublisher.publishEvent(new ProducerNotifyEvent(this, taskPendingEvent));

        return task;
    }

    @Transactional
    public Task setPrice(UUID publicId, BigDecimal assignePrice, BigDecimal closePrice) {
        Task task = taskRepository.findAndLockByPublicId(publicId).orElseThrow(() ->
                new AppException(String.format("Task with public id = %s not found", publicId)));
        if (task.getAssignePrice() != null || task.getClosePrice() != null || TaskStatus.PENDING != task.getStatus()) {
            throw new AppException(String.format("Can't set price. Invalid state of task %s", publicId));
        }
        task.setAssignePrice(assignePrice);
        task.setClosePrice(closePrice);
        task.setStatus(TaskStatus.ASSIGNED);
        task = taskRepository.save(task);
        entityManager.flush();
        sendTaskAssignedEvent(task);
        sendTaskUpdatedEvent(task);
        return task;
    }

    @Transactional
    public Task close(Long id) {
        Task task = taskRepository.findAndLockById(id).orElseThrow(() ->
                new AppException(String.format("Task with id = %d not found", id)));
        if (TaskStatus.ASSIGNED != task.getStatus()) {
            throw new AppException(String.format("Can't close. Invalid state of task id= %d", id));
        }
        task.setStatus(TaskStatus.CLOSED);
        task = taskRepository.save(task);
        entityManager.flush();
        sendTaskClosedEvent(task);
        sendTaskUpdatedEvent(task);
        return task;
    }

    @Transactional
    public void reassignAll() {
        List<Task> tasks = taskRepository.findByStatus(TaskStatus.ASSIGNED);
        List<AppUser> users = assigneeRepository.findByIsActiveIsTrue();
        for (Task task : tasks) {
            Optional<Task> lockedTaskOptional = taskRepository.findAndLockByPublicId(task.getPublicId());
            if(lockedTaskOptional.isPresent()) {
                Task lockedTask = lockedTaskOptional.get();
                if (TaskStatus.ASSIGNED != lockedTask.getStatus()) { //Status double check
                    continue;
                }
                log.debug("Task before reassign: {}", task);
                AppUser user = getRandomFromList(users);
                lockedTask.setAssignee(user);
                taskRepository.save(task);
                entityManager.flush();
                log.debug("Task after reassign: {}", task);
                sendTaskAssignedEvent(task);
                sendTaskUpdatedEvent(task);
            }
        }
    }

    private void sendTaskAssignedEvent (Task task) {
        Map<String, Object> data = new HashMap<>();
        data.put("taskPublicId", task.getPublicId());
        data.put("assigneePublicId", task.getAssignee().getPublicId());
        EventEnvelope event = EventEnvelope.preSetBuilder()
                .eventType(AppEventType.ATES_TASK_ASSIGNED)
                .producer(ATES_TASK_TRACKER_PRODUCER_NAME)
                .eventVersion("1")
                .data(data).build();
        applicationEventPublisher.publishEvent(new ProducerNotifyEvent(this, event));
    }

    private void sendTaskClosedEvent (Task task) {
        Map<String, Object> data = new HashMap<>();
        data.put("taskPublicId", task.getPublicId());
        data.put("assigneePublicId", task.getAssignee().getPublicId());
        EventEnvelope event = EventEnvelope.preSetBuilder()
                .eventType(AppEventType.ATES_TASK_CLOSED)
                .producer(ATES_TASK_TRACKER_PRODUCER_NAME)
                .eventVersion("1")
                .data(data).build();
        applicationEventPublisher.publishEvent(new ProducerNotifyEvent(this, event));
    }

    private void sendTaskUpdatedEvent (Task task) {

        EventEnvelope event = EventEnvelope.preSetBuilder()
                .eventType(AppEventType.ATES_TASK_UPDATED)
                .producer(ATES_TASK_TRACKER_PRODUCER_NAME)
                .eventVersion("1")
                .data(taskMapper.toResponceDto(task)).build();
        applicationEventPublisher.publishEvent(new ProducerNotifyEvent(this, event));
    }

    private <T> T getRandomFromList(List<T> list) {
        if (list.isEmpty()) {
            return null;
        }
        int index = ThreadLocalRandom.current().nextInt(0, (list.size() - 1) + 1);
        return list.get(index);
    }





}
