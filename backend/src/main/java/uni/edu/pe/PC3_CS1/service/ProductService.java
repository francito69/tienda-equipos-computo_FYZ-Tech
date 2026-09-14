package uni.edu.pe.PC3_CS1.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import uni.edu.pe.PC3_CS1.dto.CreateProductRequest;
import uni.edu.pe.PC3_CS1.model.Product;
import uni.edu.pe.PC3_CS1.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    
    @Autowired
    private ProductRepository productRepository;
    
    public void createProduct(CreateProductRequest request, String adminEmail) {
        logger.info("Creating product: {} for admin: {}", request.getName(), adminEmail);
        
        productRepository.save(
            request.getName(),
            request.getPrice(),
            request.getDescr(),
            adminEmail,
            request.getImgUrl()
        );
        
        logger.info("Product created successfully");
    }
    
    public Product getProductById(Long id) {
        try {
            return productRepository.findById(id);
        } catch (EmptyResultDataAccessException e) {
            logger.warn("Product not found with ID: {}", id);
            return null;
        }
    }
    
    public List<Product> searchProducts(String query) {
        logger.info("Searching products with query: {}", query);
        return productRepository.findByQuery(query);
    }
    
    public List<Product> getProductsByAdmin(String adminEmail) {
        logger.info("Getting products for admin: {}", adminEmail);
        return productRepository.findByAdminEmail(adminEmail);
    }
    
    public boolean deleteProduct(Long id) {
        logger.info("Deleting product with ID: {}", id);
        int deleted = productRepository.deleteById(id);
        return deleted > 0;
    }
}
