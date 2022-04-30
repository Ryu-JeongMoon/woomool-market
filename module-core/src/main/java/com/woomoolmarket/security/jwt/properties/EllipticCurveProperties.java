package com.woomoolmarket.security.jwt.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("application.elliptic-curve")
public record EllipticCurveProperties(String secretKey) {

}
