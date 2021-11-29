package ru.zim.ates.billing.service;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zim.ates.billing.dto.TaskFromEventDto;
import ru.zim.ates.billing.mapper.TaskMapper;
import ru.zim.ates.billing.model.Task;
import ru.zim.ates.billing.repository.TaskRepository;
import ru.zim.ates.common.exception.AppException;
import ru.zim.ates.common.standartimpl.consumer.user.repository.AppUserRepository;

@Service
public class TaskService {

    @Autowired
    TaskRepository repository;
    @Autowired
    AppUserRepository userRepository;
    @Autowired
    TaskMapper mapper;

    @Transactional
    public Task setPrice(UUID publicId, PricingService.TaskPrices prices) {
        Task task = repository.findAndLockByPublicId(publicId).orElse(null);
        if (task == null) {
            task = repository.save(Task.builder().publicId(publicId).build());
        }
        task.setAssignePrice(prices.getAssignePrice());
        task.setClosePrice(prices.getClosePrice());
        return repository.save(task);
    }

    @Transactional
    public Task closeTask(UUID publicId) {
        Task task = repository.findAndLockByPublicId(publicId).orElseThrow(() ->
                new AppException(String.format("Task with public Id = %s not found.", publicId)));
        if (Boolean.TRUE.equals(task.getIsClosed())) {
            throw new AppException(String.format("Task with public Id = %s already closed", publicId));
        }
        task.setIsClosed(true); //Версию не проверяем, это поле обновляем только отсюда и изменение не обратимое по бизнес логике.
        return repository.save(task);
    }

    @Transactional
    public Task createOrUpdate(TaskFromEventDto dto) {
        Task task = repository.findAndLockByPublicId(UUID.fromString(dto.getPublicId())).orElse(null);
        if (task == null) {
            task = mapper.fromDto(dto, userRepository);
        } else if (task.getVersion() != null && task.getVersion() > dto.getVersion()) {
            return task;
        } else {
            task = mapper.mergeFromDto(dto, task, userRepository);
        }
        return repository.save(task);
    }
}
