package com.woomoolmarket.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "mail.smtp")
public record MailProperties(int port, String host, String username, String password) {

}
