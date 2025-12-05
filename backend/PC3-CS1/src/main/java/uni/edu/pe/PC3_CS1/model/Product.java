package uni.edu.pe.PC3_CS1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    
    private Long id;
    private LocalDateTime createdAt;
    private String name;
    private Double price;
    private String descr;
    private Long createdBy;
    private String imgUrl;
}
