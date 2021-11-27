package ru.zim.ates.tasktracker.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TaskCreateRequestDto extends TaskDto {
    private Long assigneeId;
    @Builder(builderMethodName = "createBuilder")
    public TaskCreateRequestDto(String title, String description, Long assigneeId) {
        super(title, description);
        this.assigneeId = assigneeId;
    }
}
