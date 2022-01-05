package ru.zim.ates.billing.mapper;

import java.util.UUID;
import org.springframework.stereotype.Component;
import ru.zim.ates.billing.dto.TaskFromEventDto;
import ru.zim.ates.billing.model.Task;
import ru.zim.ates.common.standartimpl.consumer.user.model.AppUser;
import ru.zim.ates.common.standartimpl.consumer.user.repository.AppUserRepository;

@Component
public class TaskMapper {

    public Task fromDto(TaskFromEventDto dto, AppUserRepository repository) {
        AppUser assignee = repository.findByPublicId(UUID.fromString(dto.getAssignee())).orElseThrow(() ->
                new IllegalArgumentException(String.format("No user found with public id = %s", dto.getAssignee())));
        return Task.builder()
                .publicId(UUID.fromString(dto.getPublicId()))
                .version(dto.getVersion())
                .title(dto.getTitle())
                .assignee(assignee).build();
    }

    public Task mergeFromDto(TaskFromEventDto dto, Task task, AppUserRepository repository) {
        task.setTitle(dto.getTitle());
        task.setVersion(dto.getVersion());
        AppUser assignee = repository.findByPublicId(UUID.fromString(dto.getAssignee())).orElseThrow(() ->
                new IllegalArgumentException(String.format("No user found with public id = %s", dto.getAssignee())));
        task.setAssignee(assignee);
        return task;
    }

}
