package uni.edu.pe.PC3_CS1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    
    private Long id;
    private String name;
    private Double price;
    private String descr;
    private Long createdBy;
    private String imgUrl;
}
