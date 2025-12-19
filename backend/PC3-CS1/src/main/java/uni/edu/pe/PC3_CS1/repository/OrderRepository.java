package uni.edu.pe.PC3_CS1.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import uni.edu.pe.PC3_CS1.dto.OrderRequest;
import uni.edu.pe.PC3_CS1.model.IdQuantity;
import uni.edu.pe.PC3_CS1.model.Order;
import uni.edu.pe.PC3_CS1.model.OrderItem;

import java.util.List;

@Repository
public class OrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createOrder(OrderRequest dto) {
        String sql = "INSERT INTO \"order\" (customer_name,customer_phone,customer_email,delivery_address,total) VALUES (?,?,?,?,?) RETURNING id";
        int idOrder = jdbcTemplate.queryForObject(sql, Integer.class, dto.getCustomerName(), dto.getCustomerPhone(), dto.getCustomerEmail(), dto.getDeliveryAddress(), dto.getTotalPrice());

        sql = "INSERT INTO order_item (id_order,id_product,quantity) VALUES (?,?,?)";
        for (IdQuantity idCant : dto.getIdsCantidad()) {
            jdbcTemplate.update(sql, idOrder, idCant.getId(), idCant.getQuantity());
        }
    }

    public List<Order> getAllOrders() {
        String sql = "SELECT id, customer_name, customer_phone, customer_email, delivery_address, total FROM \"order\" ORDER BY id DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Order.class));
    }

    public List<OrderItem> getOrderItems(Long orderId) {
        String sql = "SELECT oi.id, oi.id_order, oi.id_product, oi.quantity, " +
                     "p.name as product_name, p.price as product_price, p.img_url as product_img_url " +
                     "FROM order_item oi " +
                     "INNER JOIN product p ON oi.id_product = p.id " +
                     "WHERE oi.id_order = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(OrderItem.class), orderId);
    }
}
