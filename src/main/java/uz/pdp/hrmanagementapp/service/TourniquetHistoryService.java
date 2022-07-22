package uz.pdp.hrmanagementapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.hrmanagementapp.entity.TourniquetCard;
import uz.pdp.hrmanagementapp.entity.TourniquetHistory;
import uz.pdp.hrmanagementapp.payload.ApiResponse;
import uz.pdp.hrmanagementapp.repository.TourniquetCardRepo;
import uz.pdp.hrmanagementapp.repository.TourniquetHistoryRepo;

import java.sql.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class TourniquetHistoryService {
    @Autowired
    TourniquetCardRepo tourniquetCardRepo;

    @Autowired
    TourniquetHistoryRepo tourniquetHistoryRepo;

    public ApiResponse enter(UUID id){
        Optional<TourniquetCard> optionalTourniquetCard = tourniquetCardRepo.findById(id);
        if(!optionalTourniquetCard.isPresent())
            return new ApiResponse("Ushbu card tizimda mavjud emas !",false);
        if(!optionalTourniquetCard.get().getStatus().equals("OK"))
            return new ApiResponse("Ushbu card yaroqsiz !",true);
        TourniquetHistory tourniquetHistory = new TourniquetHistory();
        tourniquetHistory.setEnteredAt(new Date(System.currentTimeMillis()));
        tourniquetHistoryRepo.save(tourniquetHistory);
        return new ApiResponse("Employee has entered !",true);

    }

    public ApiResponse exit(UUID id){
        Optional<TourniquetCard> optionalTourniquetCard = tourniquetCardRepo.findById(id);
        if(!optionalTourniquetCard.isPresent())
            return new ApiResponse("Ushbu card tizimda mavjud emas !",false);
        if(!optionalTourniquetCard.get().getStatus().equals("OK"))
            return new ApiResponse("Ushbu card yaroqsiz !",true);
        TourniquetHistory tourniquetHistory = new TourniquetHistory();
        tourniquetHistory.setExitedAt(new Date(System.currentTimeMillis()));
        tourniquetHistoryRepo.save(tourniquetHistory);
        return new ApiResponse("Employee has exited !",true);

    }
}
