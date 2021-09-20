package com.woomoolmarket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@EnableCaching
@SpringBootApplication
public class ModuleApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModuleApiApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .registerModule(new JavaTimeModule());
    }
}

/*
return Jackson2ObjectMapperBuilder.json().build()
    .enableDefaultTyping(DefaultTyping.NON_FINAL)
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    .setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
    .registerModules(new JavaTimeModule());

enableDefaultTyping 쓰니까 Type id handling not implemented 에러 뜬당
내가 필요한 건 timestamp 변환이니까 간단하게 objectMapper 에다가 직접 javaTimeModule 추가해줘서 해결
*/
