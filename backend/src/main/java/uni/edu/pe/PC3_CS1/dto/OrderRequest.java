package uni.edu.pe.PC3_CS1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uni.edu.pe.PC3_CS1.model.IdQuantity;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    String customerName;
    String customerEmail;
    String customerPhone;
    String deliveryAddress;
    float totalPrice;
    List<IdQuantity> idsCantidad;
}
