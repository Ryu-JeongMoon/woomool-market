package com.woomoolmarket.security.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.woomoolmarket.security.config.OAuth2Properties.Client;
import com.woomoolmarket.security.config.OAuth2Properties.Resource;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OAuth2ConfigTest {

  @Autowired
  OAuth2Properties oAuth2Properties;
  @Autowired
  OAuth2Config oAuth2Config;

  @Test
  @DisplayName("properties binding 성공")
  void oAuth2Properties() {
    Map<Client, Resource> registration = oAuth2Properties.registration();

    registration.keySet()
      .forEach(client -> assertAll(
        () -> assertThat(registration.get(client).clientId()).isNotBlank(),
        () -> assertThat(registration.get(client).clientName()).isNotBlank(),
        () -> assertThat(registration.get(client).clientSecret()).isNotBlank()
      ));
  }

  @Test
  @DisplayName("OAuth2SecurityConfig 설정 성공")
  void oAuth2SecurityConfig() {
    assertNotNull(oAuth2Config.clientRegistrationRepository());
  }
}