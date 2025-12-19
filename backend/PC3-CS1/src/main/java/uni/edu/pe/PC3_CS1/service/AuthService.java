package uni.edu.pe.PC3_CS1.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uni.edu.pe.PC3_CS1.config.CookieConfig;
import uni.edu.pe.PC3_CS1.dto.LoginRequest;
import uni.edu.pe.PC3_CS1.dto.LoginResponse;
import uni.edu.pe.PC3_CS1.dto.RegisterAdminRequest;
import uni.edu.pe.PC3_CS1.dto.RegisterAdminResponse;
import uni.edu.pe.PC3_CS1.jwt.JwtUtils;
import uni.edu.pe.PC3_CS1.model.Admin;
import uni.edu.pe.PC3_CS1.repository.AdminRepository;

@Service
public class AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private CookieConfig cookieConfig;
    
    public LoginResponse login(LoginRequest request, HttpServletResponse response) {
        try {
            logger.info("Login attempt for email: {}", request.getEmail());
            
            // Validate input
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                logger.warn("Login failed: Email is empty");
                return new LoginResponse(false, "Email is required", null);
            }
            
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                logger.warn("Login failed: Password is empty");
                return new LoginResponse(false, "Password is required", null);
            }
            
            // Find admin by email
            Admin admin;
            try {
                admin = adminRepository.findByEmail(request.getEmail());
            } catch (EmptyResultDataAccessException e) {
                logger.warn("Login failed: Admin not found for email: {}", request.getEmail());
                return new LoginResponse(false, "Invalid credentials", null);
            }
            
            // Verify password
            if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
                logger.warn("Login failed: Invalid password for email: {}", request.getEmail());
                return new LoginResponse(false, "Invalid credentials", null);
            }
            
            // Generate JWT token
            String token = jwtUtils.generateToken(admin.getEmail());
            
            // Create cookie with SameSite attribute
            String cookieHeader = String.format("%s=%s; Path=/; Max-Age=%d; HttpOnly; Secure; SameSite=%s",
                    cookieConfig.getCookieName(),
                    token,
                    cookieConfig.getCookieMaxAge(),
                    cookieConfig.getCookieSameSite());
            
            response.addHeader("Set-Cookie", cookieHeader);
            
            logger.info("Login successful for email: {}", request.getEmail());
            return new LoginResponse(true, "Login successful", admin.getEmail());
            
        } catch (Exception e) {
            logger.error("Error during login: {}", e.getMessage(), e);
            return new LoginResponse(false, "An error occurred during login", null);
        }
    }
    
    public RegisterAdminResponse registerAdmin(RegisterAdminRequest request) {
        try {
            logger.info("Register admin attempt for email: {}", request.getEmail());
            
            // Validate input
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                logger.warn("Registration failed: Email is empty");
                return new RegisterAdminResponse(false, "Email is required", null);
            }
            
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                logger.warn("Registration failed: Password is empty");
                return new RegisterAdminResponse(false, "Password is required", null);
            }
            
            // Validate email format
            if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                logger.warn("Registration failed: Invalid email format: {}", request.getEmail());
                return new RegisterAdminResponse(false, "Invalid email format", null);
            }
            
            // Validate password length
            if (request.getPassword().length() < 6) {
                logger.warn("Registration failed: Password too short");
                return new RegisterAdminResponse(false, "Password must be at least 6 characters", null);
            }
            
            // Hash password
            String hashedPassword = passwordEncoder.encode(request.getPassword());
            
            // Save to database
            try {
                adminRepository.save(request.getEmail(), hashedPassword);
                logger.info("Admin registered successfully: {}", request.getEmail());
                return new RegisterAdminResponse(true, "Admin registered successfully", request.getEmail());
            } catch (DataIntegrityViolationException e) {
                logger.warn("Registration failed: Email already exists: {}", request.getEmail());
                return new RegisterAdminResponse(false, "Email already exists", null);
            }
            
        } catch (Exception e) {
            logger.error("Error during registration: {}", e.getMessage(), e);
            return new RegisterAdminResponse(false, "An error occurred during registration", null);
        }
    }
}
