package ru.zim.ates.billing.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskFromEventDto {
    private String publicId;
    private String title;
    private String assignee;
    //private BigDecimal assignePrice;
    //private BigDecimal closePrice;
    private Integer version;
}
