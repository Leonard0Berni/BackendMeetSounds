package com.project.meetsounds.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.SerializationFeature;


@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Registra el módulo para manejar LocalDateTime y otros tipos de Java 8
        mapper.registerModule(new JavaTimeModule());
        // (Opcional) Configura el ObjectMapper para que use la serialización en formato ISO
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
