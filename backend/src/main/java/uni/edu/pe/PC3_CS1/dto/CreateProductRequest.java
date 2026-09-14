package uni.edu.pe.PC3_CS1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {
    private String name;
    private Double price;
    private String descr;
    private String imgUrl;
}
