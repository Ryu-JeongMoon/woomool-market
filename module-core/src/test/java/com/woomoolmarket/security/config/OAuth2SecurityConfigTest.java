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
class OAuth2SecurityConfigTest {

  @Autowired
  OAuth2Properties oAuth2Properties;
  @Autowired
  OAuth2SecurityConfig oAuth2SecurityConfig;

  @Test
  @DisplayName("properties binding 성공")
  void oAuth2Properties() {
    Map<Client, Resource> registration = oAuth2Properties.getRegistration();

    registration.keySet()
      .forEach(client -> assertAll(
        () -> assertThat(registration.get(client).getClientId()).isNotBlank(),
        () -> assertThat(registration.get(client).getClientName()).isNotBlank(),
        () -> assertThat(registration.get(client).getClientSecret()).isNotBlank()
      ));
  }

  @Test
  @DisplayName("OAuth2SecurityConfig 설정 성공")
  void oAuth2SecurityConfig() {
    assertNotNull(oAuth2SecurityConfig.clientRegistrationRepository());
  }
}