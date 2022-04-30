package com.woomoolmarket.security.jwt.factory;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.woomoolmarket.security.jwt.properties.RSA512Properties;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;

class RSA512PropertiesTest {

  @Test
  @DisplayName("application.yml jwt-rsa binding test")
  void rsa512Binding() {
    // given
    Map<String, String> properties = Map.of(
      "jwt.rsa.public-key-plain-text", "panda",
      "jwt.rsa.private-key-plain-text", "bear"
    );

    // when
    ConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
    Binder binder = new Binder(source);
    BindResult<RSA512Properties> result = binder.bind("jwt.rsa", RSA512Properties.class);
    RSA512Properties config = result.get();

    // then
    assertAll(
      () -> assertTrue(result.isBound()),
      () -> assertEquals("panda", config.publicKeyPlainText()),
      () -> assertEquals("bear", config.privateKeyPlainText())
    );
  }
}