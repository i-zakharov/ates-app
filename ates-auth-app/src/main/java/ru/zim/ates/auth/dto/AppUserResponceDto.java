package ru.zim.ates.auth.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppUserResponceDto extends AppUserUpdateRequestDto {
    @Builder(builderMethodName = "responceBuilder")
    public AppUserResponceDto(String username, Boolean isActive, String role, Long id, String publicId) {
        super(username, isActive, role, id, publicId);
    }
}
