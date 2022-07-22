package uz.pdp.hrmanagementapp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourniquetCardDto {
    private UUID companyID;
    private UUID employeeID;
}
