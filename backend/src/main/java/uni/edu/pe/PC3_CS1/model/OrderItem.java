package uni.edu.pe.PC3_CS1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private Long id;
    private Long idOrder;
    private Long idProduct;
    private Long quantity;
    private String productName;
    private Double productPrice;
    private String productImgUrl;
}
