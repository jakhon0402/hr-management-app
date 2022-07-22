package uz.pdp.hrmanagementapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hrmanagementapp.payload.ApiResponse;
import uz.pdp.hrmanagementapp.service.TourniquetHistoryService;

import java.util.UUID;

@RestController
@RequestMapping("api/historyTourniquet")
public class TourniquetHistoryController {
    @Autowired
    TourniquetHistoryService tourniquetHistoryService;

    @GetMapping(value = "/{id}")
    public HttpEntity<?> enter(@PathVariable UUID id){
        ApiResponse apiResponse = tourniquetHistoryService.enter(id);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }
    @GetMapping(value = "/{id}")
    public HttpEntity<?> exit(@PathVariable UUID id){
        ApiResponse apiResponse = tourniquetHistoryService.exit(id);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }
}
