package ru.zim.ates.common.standartimpl.consumer.user.mapper;

import java.util.UUID;
import org.springframework.stereotype.Component;
import ru.zim.ates.common.standartimpl.consumer.user.dto.AppUserFromEventDto;
import ru.zim.ates.common.standartimpl.consumer.user.dto.AppUserResponceDto;
import ru.zim.ates.common.standartimpl.consumer.user.model.AppUser;


@Component
public class AppUserMapper {

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
                .roleChangedVersion(appUser.getRoleChangedVersion())
                .build();
    }

    public AppUser fromEventDto(AppUserFromEventDto dto) {
        return AppUser.builder()
                .publicId(UUID.fromString(dto.getPublicId()))
                .username(dto.getUsername())
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .isActive(dto.getIsActive())
                .version(dto.getVersion())
                //.role(AppRole.valueOf(dto.getRole())) !!!Не обновляем роль на основании CUD события, одновляем на основе бизнес события
                .roleChangedVersion(0)
                .build();
    }

    public void mergeFromEventDto(AppUserFromEventDto dto, AppUser appUser) {
        appUser.setUsername(dto.getUsername());
        appUser.setFullName(dto.getFullName());
        appUser.setEmail(dto.getEmail());
        appUser.setIsActive(dto.getIsActive());
        //appUser.setRole(AppRole.valueOf(dto.getRole())); !!!Не обновляем роль на основании CUD события, одновляем на основе бизнес события
        appUser.setVersion(dto.getVersion());
    }

}
