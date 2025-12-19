package uni.edu.pe.PC3_CS1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private String customer_name;
    private String customer_phone;
    private String customer_email;
    private String delivery_address;
    private Double total;
}
