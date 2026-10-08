package com.chancla.chancla_lite_auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.allowed.origins}")
    private String allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Habilita CORS para todos los endpoints de la API
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins.split(",")) // Soporta múltiples orígenes separados por coma
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600); // 1 hora de cache de pre-flight
        
        System.out.println(">>> [DEBUG] CORS configurado para orígenes: " + allowedOrigins);
    }
}
