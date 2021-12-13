package com.woomoolmarket;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.woomoolmarket.config.format.DateTimeFormatModule;
import com.woomoolmarket.errors.JsonBindingResultModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.woomoolmarket", exclude = {
    org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration.class,
    org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration.class,
    org.springframework.cloud.aws.autoconfigure.context.ContextRegionProviderAutoConfiguration.class,
})
public class ModuleApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModuleApiApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
            .findAndRegisterModules()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModules(new JavaTimeModule(), new JsonBindingResultModule(), new DateTimeFormatModule());
    }
}