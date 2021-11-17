package ru.zim.ates.auth.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class AppUserCreateRequestDto extends AppUserDto {
    private String clearPassword;

    @Builder(builderMethodName = "createBuilder")
    public AppUserCreateRequestDto(String username, String fullName, String email, Boolean isActive, String role, String clearPassword, Integer version) {
        super(username, fullName, email, isActive, role, version);
        this.clearPassword = clearPassword;
    }
}
