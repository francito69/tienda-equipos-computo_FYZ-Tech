package uni.edu.pe.PC3_CS1.jwt;

import uni.edu.pe.PC3_CS1.config.CookieConfig;
import uni.edu.pe.PC3_CS1.jwt.JwtUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private CookieConfig cookieConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = extractTokenFromRequest(request);

            if (token != null && jwtUtils.isTokenValid(token)) {
                String email = jwtUtils.extractEmail(token);
                
                if (email != null && !email.isEmpty()) {
                    // Create a simple authentication token with just the email
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            email, null, null);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    logger.debug("JWT token validated for user: {}", email);
                }
            }
        } catch (Exception e) {
            // Log the exception but don't break the filter chain
            logger.error("Error processing JWT token: " + e.getMessage());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        // Buscar token en cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookieConfig.getCookieName().equals(cookie.getName()) && 
                    cookie.getValue() != null && !cookie.getValue().isEmpty()) {
                    return cookie.getValue();
                }
            }
        }

        // Buscar token en header Authorization (opcional, para compatibilidad)
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }
}
