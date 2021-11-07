package ru.zim.ates.tasktracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import ru.zim.ates.tasktracker.config.security.resourceserver.WebSecurityConfig;

@Controller
public class HomeController {

    @Autowired
    private WebSecurityConfig.AuthenticationHost authenticationHost;

    @GetMapping("/")
    public String index(ModelMap modelMap) {
        modelMap.put("login_url", authenticationHost.getLoginUrl());
        return "index";
    }

}
