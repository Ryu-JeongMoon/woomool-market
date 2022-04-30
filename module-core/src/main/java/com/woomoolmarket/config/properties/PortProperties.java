package com.woomoolmarket.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "server")
public record PortProperties(int sslPort, int httpPort) {

}

// todo, sslPort <-> port 이름 다르게 매핑 가능한지 찾아볼 것