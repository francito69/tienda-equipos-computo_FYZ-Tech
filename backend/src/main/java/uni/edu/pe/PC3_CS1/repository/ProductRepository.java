package uni.edu.pe.PC3_CS1.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import uni.edu.pe.PC3_CS1.dto.OrderRequest;
import uni.edu.pe.PC3_CS1.model.IdQuantity;
import uni.edu.pe.PC3_CS1.model.Product;

import java.util.List;

@Repository
public class ProductRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public int save(String name, Double price, String descr, String adminEmail, String imgUrl) {
        String sql = "INSERT INTO product (name, price, descr, created_by, img_url) " +
                     "VALUES (?, ?, ?, (SELECT id FROM admin WHERE email = ?), ?)";
        
        return jdbcTemplate.update(sql, name, price, descr, adminEmail, imgUrl);
    }
    
    public Product findById(Long id) {
        String sql = "SELECT * FROM product WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Product.class), id);
    }
    
    public List<Product> findByQuery(String query) {
        String sql = "SELECT * FROM product WHERE name ILIKE ? OR descr ILIKE ? ORDER BY created_at DESC";
        String searchPattern = "%" + query + "%";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Product.class), searchPattern, searchPattern);
    }
    
    public List<Product> findByAdminEmail(String adminEmail) {
        String sql = "SELECT * FROM product WHERE created_by = (SELECT id FROM admin WHERE email = ?) ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Product.class), adminEmail);
    }
    
    public int deleteById(Long id) {
        String sql = "DELETE FROM product WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
