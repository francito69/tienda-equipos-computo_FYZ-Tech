package uni.edu.pe.PC3_CS1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Admin {
    
    private Long id;
    private String email;
    private String password;
}
