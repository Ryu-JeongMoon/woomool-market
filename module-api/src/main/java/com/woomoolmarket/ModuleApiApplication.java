package com.woomoolmarket;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@EnableCaching
@SpringBootApplication
public class ModuleApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModuleApiApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder.json().build()
            .enableDefaultTyping(DefaultTyping.NON_FINAL)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
            .registerModules(new JavaTimeModule());
//        return new ObjectMapper()
//            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//            .enableDefaultTyping(DefaultTyping.NON_FINAL)
//            .setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
//            .registerModule(new JavaTimeModule());
    }
}
