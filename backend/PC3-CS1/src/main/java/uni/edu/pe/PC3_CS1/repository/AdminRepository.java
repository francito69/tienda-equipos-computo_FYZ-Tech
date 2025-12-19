package uni.edu.pe.PC3_CS1.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import uni.edu.pe.PC3_CS1.model.Admin;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class AdminRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    
    public Admin findByEmail(String email) {
        String sql = "SELECT * FROM admin WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Admin.class), email);
    }
    
    public int save(String email, String hashedPassword) {
        String sql = "INSERT INTO admin (email, password) VALUES (?, ?)";
        return jdbcTemplate.update(sql, email, hashedPassword);
    }
    
}
