package com.woomoolmarket.security.config;

import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "spring.security.oauth2.client")
public class OAuth2Properties {

  private final Map<Client, Resource> registration;

  enum Client {
    NAVER,
    KAKAO,
    GOOGLE,
    GITHUB,
    FACEBOOK
  }

  @Getter
  @ConstructorBinding
  @RequiredArgsConstructor
  static class Resource {

    private final String clientName;
    private final String clientId;
    private final String clientSecret;
  }
}
