package uni.edu.pe.PC3_CS1.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.edu.pe.PC3_CS1.dto.LoginRequest;
import uni.edu.pe.PC3_CS1.dto.LoginResponse;
import uni.edu.pe.PC3_CS1.dto.RegisterAdminRequest;
import uni.edu.pe.PC3_CS1.dto.RegisterAdminResponse;
import uni.edu.pe.PC3_CS1.service.AuthService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:4200"}, allowCredentials = "true")
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response) {
        
        logger.info("POST /auth/login - Login request received for email: {}", request.getEmail());
        
        try {
            LoginResponse loginResponse = authService.login(request, response);
            
            if (loginResponse.isSuccess()) {
                logger.info("Login successful for email: {}", request.getEmail());
                return ResponseEntity.ok(loginResponse);
            } else {
                logger.warn("Login failed for email: {} - {}", request.getEmail(), loginResponse.getMessage());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse);
            }
            
        } catch (Exception e) {
            logger.error("Error processing login request: {}", e.getMessage(), e);
            LoginResponse errorResponse = new LoginResponse(false, "Internal server error", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<RegisterAdminResponse> register(@RequestBody RegisterAdminRequest request) {
        
        logger.info("POST /auth/register - Register request received for email: {}", request.getEmail());
        
        try {
            RegisterAdminResponse registerResponse = authService.registerAdmin(request);
            
            if (registerResponse.isSuccess()) {
                logger.info("Registration successful for email: {}", request.getEmail());
                return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
            } else {
                logger.warn("Registration failed for email: {} - {}", request.getEmail(), registerResponse.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(registerResponse);
            }
            
        } catch (Exception e) {
            logger.error("Error processing registration request: {}", e.getMessage(), e);
            RegisterAdminResponse errorResponse = new RegisterAdminResponse(false, "Internal server error", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Auth service is running");
    }
}
