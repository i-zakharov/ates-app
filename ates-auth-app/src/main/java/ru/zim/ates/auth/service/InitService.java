package ru.zim.ates.auth.service;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.zim.ates.auth.model.AppUser;
import ru.zim.ates.auth.repository.AppUserRepository;
import ru.zim.ates.common.model.AppRole;

@Service
@Slf4j
public class InitService {

    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void addSomeUsersAfterStartup() {

        AppUser appUser = appUserRepository.save(AppUser.builder()
                .username("admin")
                .password(passwordEncoder.encode("qwerty"))
                .isActive(true)
                .roles(Arrays.asList(AppRole.ADMIN, AppRole.USER))
                .build());
        log.info("User added: {}", appUser);

        appUser = appUserRepository.save(AppUser.builder()
                .username("popug-kesha")
                .password(passwordEncoder.encode("qwerty"))
                .isActive(true)
                .roles(Arrays.asList(AppRole.USER))
                .build());
        log.info("User added: {}", appUser);

        appUser = appUserRepository.save(AppUser.builder()
                .username("ates-task-sys-user")
                .password(passwordEncoder.encode("qwerty"))
                .isActive(true)
                .roles(Arrays.asList(AppRole.ADMIN, AppRole.USER))
                .build());
        log.info("User added: {}", appUser);
    }
}
