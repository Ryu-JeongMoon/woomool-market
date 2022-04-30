package com.woomoolmarket.security.config;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "spring.security.oauth2.client")
public record OAuth2Properties(Map<Client, Resource> registration) {

  enum Client {
    NAVER,
    KAKAO,
    GOOGLE,
    GITHUB,
    FACEBOOK
  }

  @ConstructorBinding
  record Resource(String clientName, String clientId, String clientSecret) {

  }
}
