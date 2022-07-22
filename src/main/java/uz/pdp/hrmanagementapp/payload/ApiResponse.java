package uz.pdp.hrmanagementapp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    private String message;
    private boolean success;
    private Object object;

}
