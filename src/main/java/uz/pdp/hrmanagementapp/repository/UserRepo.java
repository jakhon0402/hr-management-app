package uz.pdp.hrmanagementapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.hrmanagementapp.entity.Company;
import uz.pdp.hrmanagementapp.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    Optional<User> findByEmailAndEmailCode(String email,String emailCode);
    Optional<User> findByEmail(String email);
    boolean existsByIdAndCompany(UUID id, Company company);
    List<User> findAllByCompanyId(UUID companyId);
}
