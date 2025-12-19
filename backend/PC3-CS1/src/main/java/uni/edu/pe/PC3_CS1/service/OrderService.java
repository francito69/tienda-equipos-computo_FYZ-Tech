package uni.edu.pe.PC3_CS1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uni.edu.pe.PC3_CS1.dto.OrderRequest;
import uni.edu.pe.PC3_CS1.model.Order;
import uni.edu.pe.PC3_CS1.model.OrderItem;
import uni.edu.pe.PC3_CS1.repository.OrderRepository;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public void createOrder(OrderRequest request) {
        orderRepository.createOrder(request);
    }

    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    public List<OrderItem> getOrderItems(Long orderId) {
        return orderRepository.getOrderItems(orderId);
    }
}
