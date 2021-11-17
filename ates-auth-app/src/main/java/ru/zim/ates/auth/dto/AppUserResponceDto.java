package ru.zim.ates.auth.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppUserResponceDto extends AppUserUpdateRequestDto {
    @Builder(builderMethodName = "responceBuilder")
    public AppUserResponceDto(String username, String fullName, String email, Boolean isActive, String role, Long id, String publicId, Integer version) {
        super(username, fullName, email, isActive, role, id, publicId, version);
    }
}
