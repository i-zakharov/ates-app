package ru.zim.ates.auth.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class AppUserCreateRequestDto extends AppUserDto {
    private String clearPassword;

    @Builder(builderMethodName = "createBuilder")
    public AppUserCreateRequestDto(String username, Boolean isActive, String role, String clearPassword) {
        super(username, isActive, role);
        this.clearPassword = clearPassword;
    }
}
