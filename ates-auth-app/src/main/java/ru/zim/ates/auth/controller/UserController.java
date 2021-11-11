package ru.zim.ates.auth.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.zim.ates.auth.mapper.AppUserMapper;
import ru.zim.ates.auth.repository.AppUserRepository;

@Controller
@RequestMapping("/auth")
public class UserController {

    @Autowired
    AppUserRepository appUserRepository;
    @Autowired
    AppUserMapper appUserMapper;

    @GetMapping("/users")
    public String getUsers() {
        return "users";
    }

    @GetMapping("/me")
    public String getMe(ModelMap modelMap, Principal principal) {
        String userName = principal.getName();
        appUserRepository.findByUsername(userName).ifPresent(appUser -> {
            modelMap.put("currentUser", appUserMapper.toResponceDto(appUser));
        });
        return "me";
    }
}
