package uz.pdp.hrmanagementapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hrmanagementapp.entity.EmployeeSalary;
import uz.pdp.hrmanagementapp.payload.ApiResponse;
import uz.pdp.hrmanagementapp.service.EmployeeSalaryService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/salary")
public class EmployeeSalaryController {
    @Autowired
    EmployeeSalaryService employeeSalaryService;

    @PreAuthorize("hasRole('DIRECTOR')")
    @GetMapping("/payment")
    public HttpEntity<?> paymentSalary(){
        ApiResponse payment = employeeSalaryService.paySalary();
        return ResponseEntity.status(payment.isSuccess()?201:409).body(payment);
    }

    @PreAuthorize("hasAnyRole('DIRECTOR')")
    @GetMapping("/salaryInfo")
    public HttpEntity<?> getInfoSalary(@PathVariable UUID id, @RequestParam Date fromDate, @RequestParam Date toDate){
        List<EmployeeSalary> employeeSalaries = employeeSalaryService.getInfoSalary(id, fromDate, toDate);
        return ResponseEntity.ok(employeeSalaries);
    }
}
