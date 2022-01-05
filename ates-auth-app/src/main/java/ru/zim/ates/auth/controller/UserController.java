package ru.zim.ates.auth.controller;

import java.security.InvalidParameterException;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.zim.ates.auth.dto.AppUserCreateRequestDto;
import ru.zim.ates.auth.dto.AppUserResponceDto;
import ru.zim.ates.auth.dto.AppUserUpdateRequestDto;
import ru.zim.ates.auth.mapper.AppUserMapper;
import ru.zim.ates.auth.model.AppUser;
import ru.zim.ates.auth.service.AppUserService;
import ru.zim.ates.common.application.model.AppRole;

@Controller
@RequestMapping("/auth/users")
public class UserController {

    @Autowired
    AppUserService appUserService;
    @Autowired
    AppUserMapper appUserMapper;

    @GetMapping("/list")
    public String getUsers(ModelMap modelMap) {
        List<AppUser> appUsers = appUserService.findAll();
        modelMap.put("appUsers", appUsers);
        return "users/list";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam Long id, ModelMap modelMap) {
        AppUser appUser = appUserService.findById(id)
                .orElseThrow(() -> new InvalidParameterException(String.format("User with id=%d not found", id)));
        AppUserResponceDto dto = appUserMapper.toResponceDto(appUser);
        modelMap.put("appUser", dto);
        modelMap.put("appRoles", buildRolesList());
        return "users/edit";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("appUser") AppUserUpdateRequestDto dto, ModelMap modelMap) {
        AppUser appUser = appUserService.update(dto);
        modelMap.put("appUser", appUserMapper.toResponceDto(appUser));
        modelMap.put("appRoles", buildRolesList());
        return "users/edit";
    }

    @GetMapping("/add")
    public String add(ModelMap modelMap) {
        modelMap.put("appUser", new AppUserCreateRequestDto());
        modelMap.put("appRoles", buildRolesList());
        return "users/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("appUser") AppUserCreateRequestDto dto) {
        AppUser appUser = appUserService.create(dto);
        return "redirect:./edit?id=" + appUser.getId();
    }

    @GetMapping("/me")
    public String getMe(ModelMap modelMap, Principal principal) {
        String userName = principal.getName();
        appUserService.findByUsername(userName).ifPresent(appUser -> {
            modelMap.put("currentUser", appUserMapper.toResponceDto(appUser));
        });
        return "me";
    }

    private List<Map<String, String>> buildRolesList() {
        return Arrays.stream(AppRole.values()).map(i -> new HashMap<String, String>() {{
            put("name", i.name());
        }}).collect(Collectors.toList());
    }
}
