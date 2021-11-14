package ru.zim.ates.auth.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppUserUpdateRequestDto extends AppUserDto {
    private Long id;
    private String publicId;

    @Builder(builderMethodName = "updateBuilder")
    public AppUserUpdateRequestDto(String username, Boolean isActive, String role, Long id, String publicId) {
        super(username, isActive, role);
        this.id = id;
        this.publicId = publicId;
    }
}
