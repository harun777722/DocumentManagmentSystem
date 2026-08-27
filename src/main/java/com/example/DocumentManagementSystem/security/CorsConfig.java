package com.example.DocumentManagementSystem.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Tüm API uçlarına izin ver
                .allowedOrigins("http://localhost:5173") // Senin React arayüzünün adresi
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // OPTIONS (Öncü Birlik) isteğine mutlaka izin vermeliyiz!
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}