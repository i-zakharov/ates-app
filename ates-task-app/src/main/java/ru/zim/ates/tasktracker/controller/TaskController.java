package ru.zim.ates.tasktracker.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.zim.ates.common.standartimpl.consumer.user.model.AppUser;
import ru.zim.ates.tasktracker.dto.TaskCreateRequestDto;
import ru.zim.ates.tasktracker.mapper.TaskMapper;
import ru.zim.ates.tasktracker.model.Task;
import ru.zim.ates.tasktracker.repository.AssigneeRepository;
import ru.zim.ates.tasktracker.service.TaskService;

@Controller
@RequestMapping("/task-tracker/tasks")
public class TaskController {

    @Autowired
    AssigneeRepository assigneeRepository;
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskMapper taskMapper;

    @GetMapping("/list")
    public String getTasks(ModelMap modelMap) {
        List<Task> tasks = taskService.findAll();
        modelMap.put("tasks", tasks);
        return "tasks/list";
    }

    @GetMapping("/card")
    public String getCard(ModelMap modelMap, @RequestParam("id") Long id) {
        modelMap.put("task", taskMapper.toResponceDto(taskService.getById(id)));
        return "tasks/card";
    }


    @GetMapping("/add")
    public String add(ModelMap modelMap) {
        modelMap.put("users", buildAssigneeList());
        modelMap.put("task", TaskCreateRequestDto.createBuilder().build());
        return "tasks/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("task") TaskCreateRequestDto dto) {
        Task task = taskService.create(dto);
        return "redirect:./card?id=" + task.getId();
    }

    @PostMapping("/reassign")
    public String reassign() {
        taskService.reassignAll();
        return "redirect:./list";
    }

    @PostMapping("/close")
    public String close(ModelMap modelMap, @RequestParam("id") Long id) {
        Task task = taskService.close(id);
        modelMap.put("task", taskMapper.toResponceDto(task));
        return "tasks/card";
    }

    private List<AppUser> buildAssigneeList() {
        return assigneeRepository.findByIsActiveIsTrue();
    }

}
