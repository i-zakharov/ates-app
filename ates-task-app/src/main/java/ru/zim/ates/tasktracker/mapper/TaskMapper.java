package ru.zim.ates.tasktracker.mapper;

import org.springframework.stereotype.Component;
import ru.zim.ates.common.standartimpl.consumer.user.model.AppUser;
import ru.zim.ates.common.standartimpl.consumer.user.repository.AppUserRepository;
import ru.zim.ates.tasktracker.dto.TaskCreateRequestDto;
import ru.zim.ates.tasktracker.dto.TaskResponceDto;
import ru.zim.ates.tasktracker.model.Task;
import ru.zim.ates.tasktracker.model.TaskStatus;

@Component
public class TaskMapper {
    public Task fromCreateDto(TaskCreateRequestDto dto, AppUserRepository appUserRepository) {
        AppUser appUserReference = appUserRepository.getOne(dto.getAssigneeId());
        return Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .assignee(appUserReference)
                .status(TaskStatus.PENDING).build();
    }

    public TaskResponceDto toResponceDto(Task task) {
        return TaskResponceDto.responceBuilder()
                .id(task.getId())
                .publicId(task.getPublicId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus().name())
                .assignee(task.getAssignee())
                .assignePrice(task.getAssignePrice())
                .closePrice(task.getClosePrice())
                .version(task.getVersion())
                .build();
    }
}
