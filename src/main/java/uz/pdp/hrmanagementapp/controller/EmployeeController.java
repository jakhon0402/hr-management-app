package uz.pdp.hrmanagementapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hrmanagementapp.entity.User;
import uz.pdp.hrmanagementapp.payload.ApiResponse;
import uz.pdp.hrmanagementapp.payload.EmployeeDto;
import uz.pdp.hrmanagementapp.payload.RegisterDto;
import uz.pdp.hrmanagementapp.service.EmployeeService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/auth")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addDirector")
    public HttpEntity<?> addDirector(@Valid @RequestBody EmployeeDto employeeDto){
        ApiResponse apiResponse = employeeService.addDirector(employeeDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    @PreAuthorize("hasRole('DIRECTOR')")
    @PostMapping("/addManager")
    public HttpEntity<?> addManager(@Valid @RequestBody RegisterDto registerDto){
        ApiResponse apiResponse = employeeService.addManager(registerDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('DIRECTOR', 'MANAGER')")
    @PostMapping("/addWorker")
    public HttpEntity<?> addWorker(@Valid @RequestBody RegisterDto registerDto){
        ApiResponse apiResponse = employeeService.addWorker(registerDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('DIRECTOR', 'MANAGER')")
    @GetMapping("/list")
    public HttpEntity<List<User>> getEmployees(){
        List<User> allEmployee = employeeService.getEmployee();
        return ResponseEntity.ok(allEmployee);
    }

}
