package ru.zim.ates.tasktracker.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.zim.ates.common.standartimpl.consumer.user.model.AppUser;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponceDto extends TaskDto {
    private Long id;
    private UUID publicId;
    private String status;
    private AppUser assignee;
    private BigDecimal price;
    private Integer version;
    @Builder(builderMethodName = "responceBuilder")
    public TaskResponceDto(String title, String description, Long id, UUID publicId, String status, AppUser assignee, BigDecimal price, Integer version) {
        super(title, description);
        this.id = id;
        this.publicId = publicId;
        this.status = status;
        this.assignee = assignee;
        this.price = price;
        this.version = version;
    }
}

