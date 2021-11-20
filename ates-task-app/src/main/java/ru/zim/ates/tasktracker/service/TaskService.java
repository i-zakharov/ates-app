package ru.zim.ates.tasktracker.service;

import java.util.List;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zim.ates.common.exception.AppException;
import ru.zim.ates.common.standartimpl.consumer.user.repository.AppUserRepository;
import ru.zim.ates.tasktracker.dto.TaskCreateRequestDto;
import ru.zim.ates.tasktracker.mapper.TaskMapper;
import ru.zim.ates.tasktracker.model.Task;
import ru.zim.ates.tasktracker.repository.TaskRepository;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private AppUserRepository userRepository;
    @Autowired
    private TaskMapper taskMapper;

    @Transactional
    public Task getById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(()-> new AppException(String.format("Task with id = %d not found.")));
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



       return task;
    }
}
