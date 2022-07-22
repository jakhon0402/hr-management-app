package uz.pdp.hrmanagementapp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {
    @NotNull
    @Size(min = 5,max = 30)
    private String firstName;

    @NotNull
    @Length(min = 5,max = 30)
    private String lastName;

    @NotNull
    private String password;

    @NotNull
    @Email
    private String email;

    @NotNull
    private UUID companyId;
}
