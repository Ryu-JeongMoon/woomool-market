package com.woomoolmarket.security.jwt.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "jwt.rsa")
public record RSA512Properties(String publicKeyPlainText, String privateKeyPlainText) {

}
