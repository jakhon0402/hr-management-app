package uz.pdp.hrmanagementapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.pdp.hrmanagementapp.entity.Task;
import uz.pdp.hrmanagementapp.entity.User;
import uz.pdp.hrmanagementapp.entity.enums.TaskStatus;
import uz.pdp.hrmanagementapp.payload.ApiResponse;
import uz.pdp.hrmanagementapp.payload.TaskDto;
import uz.pdp.hrmanagementapp.repository.TaskRepo;
import uz.pdp.hrmanagementapp.repository.UserRepo;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {
    @Autowired
    TaskRepo taskRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    JavaMailSender javaMailSender;

    public ApiResponse taskToManager(TaskDto taskDto){
        Optional<User> optionalUser = userRepo.findById(taskDto.getEmployeeId());
        if(optionalUser.isEmpty()){
            return new ApiResponse("Bunday idlik manager mavjud emas !",false);
        }
        User employee = optionalUser.get();
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setDeadLine(taskDto.getDeadline());
        task.setUser(employee);
        task.setStatus(TaskStatus.NEW);
        boolean sentEmail = sendEmail(employee.getEmail(), task.getId().toString());
        if(!sentEmail){
            return new ApiResponse("Emailga yuborishda xatolik !",false);
        }
        return new ApiResponse("Vazifa yuborildi !",true);

    }

    public ApiResponse taskToWorker(TaskDto taskDto){
        Optional<User> optionalUser = userRepo.findById(taskDto.getEmployeeId());
        if(optionalUser.isEmpty()){
            return new ApiResponse("Bunday idlik worker mavjud emas !",false);
        }
        User employee = optionalUser.get();
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setDeadLine(taskDto.getDeadline());
        task.setUser(employee);
        task.setStatus(TaskStatus.NEW);

        boolean sentEmail = sendEmail(employee.getEmail(), task.getId().toString());
        if(!sentEmail){
            return new ApiResponse("Emailga yuborishda xatolik !",false);
        }
        return new ApiResponse("Vazifa yuborildi !",true);

    }

    public ApiResponse acceptTask(UUID taskId){
        Optional<Task> optionalTask = taskRepo.findById(taskId);
        if(optionalTask.isEmpty()){
            return new ApiResponse("Bunday idlik task mavjud emas !",false);
        }
        Task task = optionalTask.get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication!=null
                &&authentication.isAuthenticated()
                &&!authentication.getPrincipal().equals("anonymousUser")){
            User currentUser = (User) authentication.getPrincipal();
            if(!currentUser.getId().equals(task.getId())){
                return new ApiResponse("Ushbu task sizga tegishli emas !",false);
            }
            task.setStatus(TaskStatus.PROCESS);
            taskRepo.save(task);
            return new ApiResponse("Task process jarayonida !",true);
        }
        return new ApiResponse("No authentication !",false);

    }

    public ApiResponse completeTask(UUID taskId){
        Optional<Task> optionalTask = taskRepo.findById(taskId);
        if(optionalTask.isEmpty())
            return new ApiResponse("Bunday idlik task mavjud emas !",false);
        Task task = optionalTask.get();
        if(new Date(System.currentTimeMillis()).before(task.getDeadLine())){
            UUID createdBy = task.getCreatedBy();
            Optional<User> optionalUser = userRepo.findById(createdBy);
            if(optionalUser.isEmpty()){
                return new ApiResponse("Vazifa yaratgan user mavjud emas !",false);
            }
            User ownerTask = optionalUser.get();
            User employee = task.getUser();
            task.setStatus(TaskStatus.COMPLETED);
            task.setCompletedAt(new Date(System.currentTimeMillis()));
            taskRepo.save(task);
            boolean sendForCompleted = sendForCompleted(task, ownerTask.getEmail(), employee.getEmail());
            if (!sendForCompleted)
                return new ApiResponse("Emailga xabar yuborishda xatolik !",false);

            return new ApiResponse("Vazifa tugatildi !",false);


        }
        task.setStatus(TaskStatus.UNCOMPLETED);
        taskRepo.save(task);
        return new ApiResponse("Deadline vaqti o'tib bo'lgan !",false);

    }

    public List<Task> getYourTasks(UUID employeeID){
        Optional<User> optionalUser = userRepo.findById(employeeID);
        if(optionalUser.isEmpty()){
            return null;
        }
        return taskRepo.findAllByUser(optionalUser.get());
    }

    public boolean sendEmail(String email,String taskId){
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("jahon77king@gmail.com");
            simpleMailMessage.setTo(email);
            simpleMailMessage.setSubject("Yangi vazifa");
            simpleMailMessage.setText("<a href='http://localhost:8080/api/auth/acceptTask?taskCode="+taskId+"'></a>");
            javaMailSender.send(simpleMailMessage);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendForCompleted(Task task, String emailTo, String emailFrom){
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(emailFrom);
            simpleMailMessage.setTo(emailTo);
            simpleMailMessage.setSubject("Vazifa bajarildi !");
            simpleMailMessage.setText("<p>Task: " + task.getName() + "</p>" +
                    "<p>Description: " + task.getDescription() + "</p>" +
                    "<p>Completed: " + task.getCompletedAt() + "</p>");
            javaMailSender.send(simpleMailMessage);
            return true;
        } catch (Exception ignored) {
            return false;
        }

    }

}
