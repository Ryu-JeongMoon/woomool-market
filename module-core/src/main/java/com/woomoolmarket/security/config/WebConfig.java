package com.woomoolmarket.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.security.config.xss.HTMLCharacterEscapes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedMethods("GET", "POST", "PATCH", "DELETE")
            .allowedOrigins("http://localhost:8080");
    }

    @Bean
    public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
        objectMapper.getFactory().setCharacterEscapes(new HTMLCharacterEscapes());
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }
}