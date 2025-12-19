package uni.edu.pe.PC3_CS1.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    // Valores hardcodeados para evitar problemas de configuración
    private final String secret = "xY6i+vJb3OT90bMdL5EiyXKZgRwr0czVK8cvBwnE7UM=";
    private final long expiration = 3600000; // 1 hora en milisegundos

    public JwtUtils() {
        logger.info("JwtUtils initialized with secret length: {} and expiration: {}", 
                   secret.length(), expiration);
    }

    public String generateToken(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username no puede ser null o vacío");
        }
        
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateVerificationToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3*expiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        logger.info("=== JwtUtils.extractEmail() called ===");
        logger.info("Token received: {}", token != null ? "NOT NULL" : "NULL");
        
        if (token == null || token.trim().isEmpty()) {
            logger.error("Token is null or empty");
            throw new IllegalArgumentException("Token no puede ser null o vacío");
        }
        
        try {
            logger.info("Parsing JWT token...");
            var claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            String email = claims.getSubject();
            logger.info("Email extracted successfully from token: {}", email);
            return email;
            
        } catch (JwtException e) {
            logger.error("JWT parsing error in extractEmail: {}", e.getMessage(), e);
            throw new JwtException("Token inválido: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error in extractEmail: {}", e.getMessage(), e);
            throw new JwtException("Error procesando token: " + e.getMessage());
        }
    }

    public boolean isTokenValid(String token) {
        logger.info("=== JwtUtils.isTokenValid() called ===");
        logger.info("Token received: {}", token != null ? "NOT NULL" : "NULL");
        
        if (token == null || token.trim().isEmpty()) {
            logger.info("Token is null or empty");
            return false;
        }
        
        try {
            // Parsear el token para verificar que es válido
            var claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            logger.info("Token parsed successfully");
            
            // Verificar que la fecha de expiración no haya pasado
            Date expirationDate = claims.getExpiration();
            if (expirationDate == null) {
                logger.info("Token has no expiration date");
                return false; // Token sin fecha de expiración
            }
            
            Date now = new Date();
            boolean isValid = !expirationDate.before(now);
            
            logger.info("Token expiration date: {}", expirationDate);
            logger.info("Current date: {}", now);
            logger.info("Token is expired: {}", expirationDate.before(now));
            logger.info("Token is valid: {}", isValid);
            
            return isValid;
            
        } catch (JwtException e) {
            System.out.println("JWT token: "+token);
            logger.error("JWT parsing error: {}", e.getMessage());
            return false;
        }
    }
}
