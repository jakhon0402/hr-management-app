package uz.pdp.hrmanagementapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.hrmanagementapp.entity.EmployeeSalary;
import uz.pdp.hrmanagementapp.entity.User;
import uz.pdp.hrmanagementapp.payload.ApiResponse;
import uz.pdp.hrmanagementapp.repository.EmployeeSalaryRepo;
import uz.pdp.hrmanagementapp.repository.UserRepo;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeSalaryService {
    @Autowired
    EmployeeSalaryRepo employeeSalaryRepo;

    @Autowired
    UserRepo userRepo;

    static final double SALARY_AMOUNT = 1_000.0;

    public ApiResponse paySalary(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null
                &&authentication.isAuthenticated()
                &&authentication.getPrincipal().equals("anonymousUser"));
        User currentEmployee = (User) authentication.getPrincipal();

        List<User> employeeList = userRepo.findAllByCompanyId(currentEmployee.getCompany().getId());
        for(User employee:employeeList){
            EmployeeSalary employeeSalary = new EmployeeSalary();
            employeeSalary.setSalary(SALARY_AMOUNT);
            employeeSalary.setUser(employee);
            employeeSalary.setPaymentDate(new Date(System.currentTimeMillis()));
            employeeSalaryRepo.save(employeeSalary);
        }
        return new ApiResponse("Oylik to'lovlar amalga oshirildi!",true);
    }

    public List<EmployeeSalary> getInfoSalary(UUID id, Date start,Date end){
        Optional<User> optionalUser = userRepo.findById(id);
        if(!optionalUser.isPresent())
            return null;
        return employeeSalaryRepo.getEmployeeSalary(start,end,id);
    }

}
