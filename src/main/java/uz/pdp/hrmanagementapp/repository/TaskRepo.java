package uz.pdp.hrmanagementapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.hrmanagementapp.entity.Task;
import uz.pdp.hrmanagementapp.entity.User;

import java.util.List;
import java.util.UUID;

public interface TaskRepo extends JpaRepository<Task, UUID> {
    List<Task> findAllByUser(User user);
}
