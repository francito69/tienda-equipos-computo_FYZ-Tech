package uni.edu.pe.PC3_CS1.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.edu.pe.PC3_CS1.config.CookieConfig;
import uni.edu.pe.PC3_CS1.dto.CreateProductRequest;
import uni.edu.pe.PC3_CS1.dto.ProductResponse;
import uni.edu.pe.PC3_CS1.jwt.JwtUtils;
import uni.edu.pe.PC3_CS1.model.Product;
import uni.edu.pe.PC3_CS1.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = {"http://localhost:4200"}, allowCredentials = "true")
public class ProductController {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private CookieConfig cookieConfig;
    
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody CreateProductRequest request,
            @CookieValue(name = "${app.cookie.name}") String token) {
        
        logger.info("POST /products - Creating product: {}", request.getName());
        
        try {
            // Extract admin email from JWT token
            String adminEmail = jwtUtils.extractEmail(token);
            logger.info("Admin email extracted from token: {}", adminEmail);
            
            productService.createProduct(request, adminEmail);
            
            ProductResponse response = new ProductResponse(true, "Product created successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            logger.error("Error creating product: {}", e.getMessage(), e);
            
            ProductResponse response = new ProductResponse(false, "Error creating product: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        logger.info("GET /products/{} - Fetching product", id);
        
        Product product = productService.getProductById(id);
        
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String query) {
        logger.info("GET /products/search?query={} - Searching products", query);
        
        List<Product> products = productService.searchProducts(query);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/my-products")
    public ResponseEntity<List<Product>> getMyProducts(
            @CookieValue(name = "${app.cookie.name}") String token) {
        
        logger.info("GET /products/my-products - Fetching products for authenticated admin");
        
        try {
            // Extract admin email from JWT token
            String adminEmail = jwtUtils.extractEmail(token);
            logger.info("Admin email extracted from token: {}", adminEmail);
            
            List<Product> products = productService.getProductsByAdmin(adminEmail);
            return ResponseEntity.ok(products);
            
        } catch (Exception e) {
            logger.error("Error fetching admin products: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable Long id) {
        logger.info("DELETE /products/{} - Deleting product", id);
        
        boolean deleted = productService.deleteProduct(id);
        
        if (deleted) {
            ProductResponse response = new ProductResponse(true, "Product deleted successfully");
            return ResponseEntity.ok(response);
        } else {
            ProductResponse response = new ProductResponse(false, "Product not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
