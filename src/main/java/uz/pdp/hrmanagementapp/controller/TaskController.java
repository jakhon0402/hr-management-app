package uz.pdp.hrmanagementapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hrmanagementapp.entity.Task;
import uz.pdp.hrmanagementapp.entity.User;
import uz.pdp.hrmanagementapp.payload.ApiResponse;
import uz.pdp.hrmanagementapp.payload.TaskDto;
import uz.pdp.hrmanagementapp.service.TaskService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/task")
public class TaskController {
    @Autowired
    TaskService taskService;

    @GetMapping("/all")
    public HttpEntity<?> getYourTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User employee = (User) authentication.getPrincipal();
        List<Task> taskByEmployee = taskService.getYourTasks(employee.getId());
        return ResponseEntity.ok(taskByEmployee);
    }

    @PreAuthorize("hasRole('DIRECTOR')")
    @PostMapping("/manager")
    public HttpEntity<?> taskDirector(@RequestBody TaskDto taskDto){
        ApiResponse apiResponse = taskService.taskToManager(taskDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('DIRECTOR', 'HR_MANAGER')")
    @PostMapping("/worker")
    public HttpEntity<?> taskManager(@RequestBody TaskDto dto){
        ApiResponse apiResponse = taskService.taskToWorker(dto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    @GetMapping("/accept/{id}")
    public HttpEntity<?> acceptTask(@PathVariable UUID id){
        ApiResponse apiResponse = taskService.acceptTask(id);
        return ResponseEntity.status(apiResponse.isSuccess()?203:409).body(apiResponse);
    }

    @GetMapping("/completed/{id}")
    public HttpEntity<?> completedTask(@PathVariable UUID id){
        ApiResponse completed = taskService.completeTask(id);
        return ResponseEntity.status(completed.isSuccess()?202:409).body(completed);
    }
}
