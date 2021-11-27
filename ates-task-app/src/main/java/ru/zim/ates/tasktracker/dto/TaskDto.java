package ru.zim.ates.tasktracker.dto;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zim.ates.tasktracker.model.TaskStatus;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private String title;
    private String description;
}
