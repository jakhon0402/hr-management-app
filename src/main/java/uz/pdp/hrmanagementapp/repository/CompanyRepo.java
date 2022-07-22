package uz.pdp.hrmanagementapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import uz.pdp.hrmanagementapp.entity.Company;

import java.util.UUID;

@RepositoryRestResource(path = "/company")
public interface CompanyRepo extends JpaRepository<Company, UUID> {
}
