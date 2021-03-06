package ru.zim.ates.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserDto {
    private String username;
    private String fullName;
    private String email;
    private Boolean isActive;
    private String role;
    private Integer version;
}
