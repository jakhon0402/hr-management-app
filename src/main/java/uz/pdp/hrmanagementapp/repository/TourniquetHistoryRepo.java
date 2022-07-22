package uz.pdp.hrmanagementapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.hrmanagementapp.entity.TourniquetCard;
import uz.pdp.hrmanagementapp.entity.TourniquetHistory;

import java.util.UUID;

public interface TourniquetHistoryRepo extends JpaRepository<TourniquetHistory, UUID> {

}
