// backend/src/main/java/com/fyztech/tienda/config/WebConfig.java
package com.fyztech.tienda.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Servir QR images
        String qrPath = Paths.get("uploads/qr").toAbsolutePath().toString();
        registry.addResourceHandler("/uploads/qr/**")
                .addResourceLocations("file:" + qrPath + "/");

        // Servir comprobantes
        String comprobantesPath = Paths.get("uploads/comprobantes").toAbsolutePath().toString();
        registry.addResourceHandler("/uploads/comprobantes/**")
                .addResourceLocations("file:" + comprobantesPath + "/");
    }
}