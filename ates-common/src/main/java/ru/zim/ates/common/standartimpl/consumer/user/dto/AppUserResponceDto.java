package ru.zim.ates.common.standartimpl.consumer.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppUserResponceDto extends AppUserFromEventDto {
    private Long id;
    private Integer roleChangedVersion;

    @Builder(builderMethodName = "responceBuilder")
    public AppUserResponceDto(String publicId, String username, String fullName, String email, Boolean isActive, String role, Integer version, Long id, Integer roleChangedVersion) {
        super(publicId, username, fullName, email, isActive, role, version);
        this.id = id;
        this.roleChangedVersion = roleChangedVersion;
    }
}
