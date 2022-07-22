package uz.pdp.hrmanagementapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.hrmanagementapp.entity.TourniquetCard;

import java.util.UUID;

public interface TourniquetCardRepo extends JpaRepository<TourniquetCard, UUID> {

}
