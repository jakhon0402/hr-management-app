package uz.pdp.hrmanagementapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.hrmanagementapp.entity.EmployeeSalary;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

public interface EmployeeSalaryRepo extends JpaRepository<EmployeeSalary, UUID> {
    @Query(nativeQuery = true,value = "Select * from employee_salary sl" +
            "join users emp on sl.employee_id =:id" +
            "where sl.date_time between :start and :ended" +
            "or" +
            "Select * from employee_salary sal" +
            "where sl.date_time  between :start and :ended")
    List<EmployeeSalary> getEmployeeSalary(@Param("start")Date start, @Param("end") Date ended, @Param("id") UUID id);
}
