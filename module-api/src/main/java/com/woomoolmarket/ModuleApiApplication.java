package com.woomoolmarket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.woomoolmarket.errors.JsonBindingResultModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@EnableCaching
@SpringBootApplication(scanBasePackages = "com.woomoolmarket",
    exclude = {
        org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration.class,
        org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration.class,
        org.springframework.cloud.aws.autoconfigure.context.ContextRegionProviderAutoConfiguration.class
    })
public class ModuleApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModuleApiApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .registerModules(new JavaTimeModule(), new JsonBindingResultModule());
    }
}

/*
return Jackson2ObjectMapperBuilder.json().build()
    .enableDefaultTyping(DefaultTyping.NON_FINAL)
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    .setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
    .registerModules(new JavaTimeModule());
*/
