package ru.zim.ates.tasktracker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/task-tracker")
public class TaskController {
    @GetMapping("/tasks")
    public String getTasks() {
        return "tasks";
    }
}
