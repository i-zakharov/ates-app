package ru.zim.ates.auth.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.zim.ates.auth.dto.AppUserCreateRequestDto;
import ru.zim.ates.auth.dto.AppUserResponceDto;
import ru.zim.ates.auth.dto.AppUserUpdateRequestDto;
import ru.zim.ates.auth.model.AppUser;
import ru.zim.ates.common.application.model.AppRole;

@Component
public class AppUserMapper {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public AppUserResponceDto toResponceDto(AppUser appUser) {
        return AppUserResponceDto.responceBuilder()
                .id(appUser.getId())
                .publicId(appUser.getPublicId().toString())
                .username(appUser.getUsername())
                .fullName(appUser.getFullName())
                .email(appUser.getEmail())
                .isActive(appUser.getIsActive())
                .role(appUser.getRole().name())
                .version(appUser.getVersion())
                .build();
    }

    public AppUser fromCreateDto(AppUserCreateRequestDto dto) {
        return AppUser.builder()
                .username(dto.getUsername())
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .isActive(dto.getIsActive())
                .role(AppRole.valueOf(dto.getRole()))
                .password(passwordEncoder.encode(dto.getClearPassword()))
                .build();
    }

    public void mergeFromUpdateDto(AppUserUpdateRequestDto dto, AppUser appUser) {
        appUser.setUsername(dto.getUsername());
        appUser.setFullName(dto.getFullName());
        appUser.setEmail(dto.getEmail());
        appUser.setIsActive(dto.getIsActive());
        appUser.setRole(AppRole.valueOf(dto.getRole()));
        appUser.setVersion(dto.getVersion());
    }

    public AppUserUpdateRequestDto toUpdateDto(AppUser appUser) {
        return AppUserUpdateRequestDto.updateBuilder()
                .id(appUser.getId())
                .publicId(appUser.getPublicId().toString())
                .username(appUser.getUsername())
                .fullName(appUser.getFullName())
                .email(appUser.getEmail())
                .isActive(appUser.getIsActive())
                .role(appUser.getRole().name())
                .version(appUser.getVersion())
                .build();
    }
}
