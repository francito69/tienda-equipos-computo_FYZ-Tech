package uni.edu.pe.PC3_CS1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.edu.pe.PC3_CS1.dto.OrderRequest;
import uni.edu.pe.PC3_CS1.dto.ProductResponse;
import uni.edu.pe.PC3_CS1.model.Order;
import uni.edu.pe.PC3_CS1.model.OrderItem;
import uni.edu.pe.PC3_CS1.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = {"http://localhost:4200"}, allowCredentials = "true")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<ProductResponse> createOrder(@RequestBody OrderRequest request) {
        logger.info("POST /orders - Creating new order for customer: {}", request.getCustomerName());

        try {
            orderService.createOrder(request);
            ProductResponse response = new ProductResponse(true, "Orden creada exitosamente");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error creating order: {}", e.getMessage(), e);
            ProductResponse response = new ProductResponse(false, "Error al crear la orden: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        logger.info("GET /orders - Retrieving all orders");

        try {
            List<Order> orders = orderService.getAllOrders();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            logger.error("Error retrieving orders: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<OrderItem>> getOrderItems(@PathVariable Long orderId) {
        logger.info("GET /orders/{}/items - Retrieving order items", orderId);

        try {
            List<OrderItem> items = orderService.getOrderItems(orderId);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            logger.error("Error retrieving order items: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
