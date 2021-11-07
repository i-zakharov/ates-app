package ru.zim.ates.auth.mapper;

import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import ru.zim.ates.auth.dto.AppUserResponceDto;
import ru.zim.ates.auth.model.AppUser;
import ru.zim.ates.common.model.AppRole;

@Component
public class AppUserMapper {

    public AppUserResponceDto toResponceDto(AppUser appUser) {
        return AppUserResponceDto.builder()
                .id(appUser.getId().toString())
                .username(appUser.getUsername())
                .isActive(appUser.getIsActive())
                .roles(appUser.getRoles().stream().map(AppRole::name).collect(Collectors.toList())).build();
    }
}
