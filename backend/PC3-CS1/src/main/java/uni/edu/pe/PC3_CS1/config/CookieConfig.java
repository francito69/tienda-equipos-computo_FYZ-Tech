package uni.edu.pe.PC3_CS1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CookieConfig {
    
    @Value("${app.cookie.secure:true}")
    private boolean cookieSecure;
    
    @Value("${app.cookie.same-site:None}")
    private String cookieSameSite;
    
    @Value("${app.cookie.max-age:3600}")
    private int cookieMaxAge;
    
    @Value("${app.cookie.name:LuminousToken}")
    private String cookieName;
    
    @Value("${app.cookie.http-only:true}")
    private boolean cookieHttpOnly;
    
    public boolean isCookieSecure() {
        return cookieSecure;
    }
    
    public String getCookieSameSite() {
        return cookieSameSite;
    }
    
    public int getCookieMaxAge() {
        return cookieMaxAge;
    }
    
    public String getCookieName() {
        return cookieName;
    }
    
    public boolean isCookieHttpOnly() {
        return cookieHttpOnly;
    }
} 