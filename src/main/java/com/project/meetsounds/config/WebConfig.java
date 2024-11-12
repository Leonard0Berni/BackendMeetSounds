package com.project.meetsounds.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Permite CORS en todas las rutas
                .allowedOrigins("http://localhost:5173")  // Permitir solo el origen del frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Métodos permitidos
                .allowedHeaders("*")  // Permitir todos los encabezados
                .allowCredentials(true);  // Permitir credenciales (cookies, etc.)
    }
}
