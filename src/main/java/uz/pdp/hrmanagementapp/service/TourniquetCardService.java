package uz.pdp.hrmanagementapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.hrmanagementapp.entity.Company;
import uz.pdp.hrmanagementapp.entity.TourniquetCard;
import uz.pdp.hrmanagementapp.entity.User;
import uz.pdp.hrmanagementapp.payload.ApiResponse;
import uz.pdp.hrmanagementapp.payload.TourniquetCardDto;
import uz.pdp.hrmanagementapp.repository.CompanyRepo;
import uz.pdp.hrmanagementapp.repository.TourniquetCardRepo;
import uz.pdp.hrmanagementapp.repository.UserRepo;

import java.sql.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class TourniquetCardService {
    @Autowired
    TourniquetCardRepo tourniquetCardRepo;

    @Autowired
    CompanyRepo companyRepo;

    @Autowired
    UserRepo userRepo;

    static final long EXPIRE_TIME = 31_556_952_000L; // 1 year

    public ApiResponse addTourniquetCard(TourniquetCardDto tourniquetCardDto){
        Optional<Company> optionalCompany = companyRepo.findById(tourniquetCardDto.getCompanyID());
        if(!optionalCompany.isPresent())
            return new ApiResponse("Bunday company mavjud emas !",false);
        Optional<User> optionalUser = userRepo.findById(tourniquetCardDto.getEmployeeID());
        if(!optionalUser.isPresent())
            return new ApiResponse("Bunday idlik employee mavjud emas !",false);
        User employee = optionalUser.get();
        if(userRepo.existsByIdAndCompany(employee.getId(),optionalCompany.get()))
            return new ApiResponse("Ushbu employee ushbu company ga tegishli emas !",false);
        TourniquetCard tourniquetCard = new TourniquetCard();
        tourniquetCard.setCompany(optionalCompany.get());
        tourniquetCard.setUser(employee);
        tourniquetCard.setExpireDate(new Date(System.currentTimeMillis()+EXPIRE_TIME));
        tourniquetCard.setStatus("OK");
        tourniquetCardRepo.save(tourniquetCard);
        return new ApiResponse("Yangi turniket card qo'shildi !",true);
    }

    public ApiResponse editTourniquetCard(TourniquetCardDto tourniquetCardDto, UUID id){
        Optional<TourniquetCard> tourniquetCardOptional = tourniquetCardRepo.findById(id);
        if(!tourniquetCardOptional.isPresent())
            return new ApiResponse("Ushbu turniket card mavjud emas !",false);
        Optional<Company> optionalCompany = companyRepo.findById(tourniquetCardDto.getCompanyID());
        if(!optionalCompany.isPresent())
            return new ApiResponse("Bunday company mavjud emas !",false);
        Optional<User> optionalUser = userRepo.findById(tourniquetCardDto.getEmployeeID());
        if(!optionalCompany.isPresent())
            return new ApiResponse("Bunday idlik employee mavjud emas !",false);
        User employee = optionalUser.get();
        if(userRepo.existsByIdAndCompany(employee.getId(),optionalCompany.get()))
            return new ApiResponse("Ushbu employee ushbu company ga tegishli emas !",false);
        TourniquetCard tourniquetCard = tourniquetCardOptional.get();
        tourniquetCard.setCompany(optionalCompany.get());
        tourniquetCard.setUser(employee);
        tourniquetCard.setExpireDate(new Date(System.currentTimeMillis()+EXPIRE_TIME));
        tourniquetCard.setStatus("OK");
        tourniquetCardRepo.save(tourniquetCard);
        return new ApiResponse("Turniket tahrirlandi !",true);
    }

    public ApiResponse deleteTourniquetCard(UUID id){
        Optional<TourniquetCard> tourniquetCardOptional = tourniquetCardRepo.findById(id);
        if(!tourniquetCardOptional.isPresent())
            return new ApiResponse("Ushbu turniket card mavjud emas !",false);
        tourniquetCardRepo.delete(tourniquetCardOptional.get());
        return new ApiResponse("Turniket o'chirildi !",true);
    }

}
