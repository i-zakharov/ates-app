package ru.zim.ates.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.zim.ates.auth.dto.AppUserCreateRequestDto;
import ru.zim.ates.auth.mapper.AppUserMapper;
import ru.zim.ates.auth.model.AppUser;
import ru.zim.ates.common.model.AppRole;

@Service
@Slf4j
public class InitService {

    @Autowired
    private AppUserService appUserService;
    @Autowired
    private AppUserMapper mapper;

    @EventListener(ApplicationReadyEvent.class)
    public void addSomeUsersAfterStartup() {

        AppUser appUser = appUserService.create(AppUserCreateRequestDto.createBuilder()
                .username("admin")
                .fullName("Super Root")
                .email("admin@ates.popug")
                .clearPassword("qwerty")
                .isActive(true)
                .role(AppRole.ADMIN.name()).build());
        log.info("User added: {}", appUser);

        appUser = appUserService.create(AppUserCreateRequestDto.createBuilder()
                .username("kesha")
                .fullName("Kesha Ivanov")
                .email("admin@ates.popug")
                .clearPassword("qwerty")
                .isActive(true)
                .role(AppRole.USER.name()).build());
        log.info("User added: {}", appUser);

        appUser = appUserService.create(AppUserCreateRequestDto.createBuilder()
                .username("ates-task-sys-user")
                .clearPassword("qwerty")
                .isActive(true)
                .role(AppRole.ADMIN.name()).build());
        log.info("User added: {}", appUser);

        //А теперь поменяем роль для одно из пользователей
        appUser.setRole(AppRole.MANAGER);
        appUserService.update(mapper.toUpdateDto(appUser));

    }
}
