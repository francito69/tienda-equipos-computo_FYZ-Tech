package uni.edu.pe.PC3_CS1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterAdminResponse {
    private boolean success;
    private String message;
    private String email;
}
