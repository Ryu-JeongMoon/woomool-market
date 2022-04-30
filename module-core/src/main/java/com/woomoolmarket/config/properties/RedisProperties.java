package com.woomoolmarket.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("spring.redis")
public record RedisProperties(int port, String host) {

}
