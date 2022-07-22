package uz.pdp.hrmanagementapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hrmanagementapp.payload.ApiResponse;
import uz.pdp.hrmanagementapp.payload.TourniquetCardDto;
import uz.pdp.hrmanagementapp.service.TourniquetCardService;

import java.util.UUID;

@RestController
@RequestMapping("api/tourniquetCard")
public class TourniquetCardController {
    @Autowired
    TourniquetCardService tourniquetCardService;

    @PreAuthorize("hasAnyRole('DIRECTOR', 'HR_MANAGER')")
    @PostMapping("/create")
    public HttpEntity<?> createCard(@RequestBody TourniquetCardDto dto){
        ApiResponse apiResponse = tourniquetCardService.addTourniquetCard(dto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('DIRECTOR', 'HR_MANAGER')")
    @PutMapping("/edit")
    public HttpEntity<?> editCard(@RequestBody TourniquetCardDto dto, @PathVariable UUID id){
        ApiResponse apiResponse = tourniquetCardService.editTourniquetCard(dto, id);
        return ResponseEntity.status(apiResponse.isSuccess()?202:409).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('DIRECTOR', 'HR_MANAGER')")
    @GetMapping("/delete")
    public HttpEntity<?> deleteCard(@PathVariable UUID id){
        ApiResponse apiResponse = tourniquetCardService.deleteTourniquetCard(id);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }
}
