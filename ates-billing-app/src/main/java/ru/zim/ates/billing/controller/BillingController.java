package ru.zim.ates.billing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/billing")
public class BillingController {
    @GetMapping("/billing-dashbord")
    public String getTasks() {
        return "billing-dashbord";
    }
}
