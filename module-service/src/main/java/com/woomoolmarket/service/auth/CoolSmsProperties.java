package com.woomoolmarket.service.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "coolsms")
public class CoolSmsProperties {

  private final String email;
  private final String apiKey;
  private final String apiSecret;
  private final String phoneNumber;
}
