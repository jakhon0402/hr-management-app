package uz.pdp.hrmanagementapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.hrmanagementapp.entity.Role;
import uz.pdp.hrmanagementapp.entity.enums.RoleName;

public interface RoleRepo extends JpaRepository<Role,Integer> {
    Role findByRoleName(RoleName roleName);
}
