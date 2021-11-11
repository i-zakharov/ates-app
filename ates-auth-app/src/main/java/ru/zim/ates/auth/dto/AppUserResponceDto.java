package ru.zim.ates.auth.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AppUserResponceDto {
    private String id;
    private String username;
    private Boolean isActive;
    private List<String> roles;
}
