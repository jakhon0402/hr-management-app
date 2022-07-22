package uz.pdp.hrmanagementapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.hrmanagementapp.entity.Company;
import uz.pdp.hrmanagementapp.entity.User;
import uz.pdp.hrmanagementapp.entity.enums.RoleName;
import uz.pdp.hrmanagementapp.payload.ApiResponse;
import uz.pdp.hrmanagementapp.payload.EmployeeDto;
import uz.pdp.hrmanagementapp.payload.RegisterDto;
import uz.pdp.hrmanagementapp.repository.CompanyRepo;
import uz.pdp.hrmanagementapp.repository.RoleRepo;
import uz.pdp.hrmanagementapp.repository.UserRepo;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    RoleRepo roleRepo;

    @Lazy
    @Autowired
    JavaMailSender javaMailSender;

    @Lazy
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CompanyRepo companyRepo;

    public ApiResponse addDirector(EmployeeDto employeeDto){
        Optional<Company> optionalCompany = companyRepo.findById(employeeDto.getCompanyId());
        if(!optionalCompany.isPresent()){
            return new ApiResponse("Bunday company mavjud emas !",false);
        }
        boolean existsByEmail = userRepo.existsByEmail(employeeDto.getEmail());
        if(existsByEmail){
            return new ApiResponse("Bunday email lik direktor mavjud !",false);
        }
        String emailCode = UUID.randomUUID().toString();
        boolean isSentEmail = sendEmail(employeeDto.getEmail(), emailCode);
        if(!isSentEmail){
            return new ApiResponse("Emailga yuborishda xatolik!",false);
        }
        User user = new User();
        user.setFirstName(employeeDto.getFirstName());
        user.setLastName(employeeDto.getLastName());
        user.setEmail(employeeDto.getEmail());
        user.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
        user.setRoles(Collections.singleton(roleRepo.findByRoleName(RoleName.DIRECTOR)));
        user.setEmailCode(emailCode);
        user.setCompany(optionalCompany.get());
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication!=null
//                &&authentication.isAuthenticated()
//                &&!authentication.getPrincipal().equals("anonymousUser")){
//            User currentUser = (User) authentication.getPrincipal();
//            user.setCompany(currentUser.getCompany());
//        }
        userRepo.save(user);
        return new ApiResponse("Direktor muvaffaqiyatli qo'shildi!",true);
    }

    public ApiResponse addManager(RegisterDto registerDto){
        return addEmployee(registerDto, RoleName.MANAGER);
    }

    public ApiResponse addHrManager(RegisterDto registerDto){
        return addEmployee(registerDto, RoleName.HR_MANAGER);
    }

    public ApiResponse addWorker(RegisterDto registerDto){
        return addEmployee(registerDto, RoleName.WORKER);
    }

    public ApiResponse addEmployee(RegisterDto registerDto, RoleName role){
        boolean existsByEmail = userRepo.existsByEmail(registerDto.getEmail());
        if(existsByEmail){
            return new ApiResponse("Bunday email lik "+ role.toString() +" mavjud !",false);
        }
        String emailCode = UUID.randomUUID().toString();
        boolean isSentEmail = sendEmail(registerDto.getEmail(), emailCode);
        if(!isSentEmail){
            return new ApiResponse("Emailga yuborishda xatolik!",false);
        }
        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRoles(Collections.singleton(roleRepo.findByRoleName(role)));
        user.setEmailCode(emailCode);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication!=null
                &&authentication.isAuthenticated()
                &&!authentication.getPrincipal().equals("anonymousUser")){
            User currentUser = (User) authentication.getPrincipal();
            user.setCompany(currentUser.getCompany());
        }
        userRepo.save(user);
        return new ApiResponse(role.toString() + " muvaffaqiyatli qo'shildi!",true);
    }

    public List<User> getEmployee(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null
                &&authentication.isAuthenticated()
                &&authentication.getPrincipal().equals("anonymousUser"));
        User currentEmployee = (User) authentication.getPrincipal();

        List<User> employeeList = userRepo.findAllByCompanyId(currentEmployee.getCompany().getId());
        return employeeList;
    }


    public boolean sendEmail(String sendingEmail, String emailCode){
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("jahon99king@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Akkountni tasdiqlash");
            mailMessage.setText("<a href='http://localhost:8080/api/auth/verifyEmail?emailCode="+emailCode+"&email="+sendingEmail+"'></a>");
            javaMailSender.send(mailMessage);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
