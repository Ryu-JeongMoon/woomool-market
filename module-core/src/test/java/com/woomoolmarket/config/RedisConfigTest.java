package com.woomoolmarket.config;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.woomoolmarket.config.properties.RedisProperties;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;

class RedisConfigTest {

  @Test
  @DisplayName("application.yml redis binding test")
  void awsS3Binding() {
    // given
    Map<String, Object> properties = Map.of(
      "redis.port", 6379,
      "redis.host", "localhost"
    );

    // when
    ConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
    Binder binder = new Binder(source);
    BindResult<RedisProperties> result = binder.bind("redis", RedisProperties.class);
    RedisProperties config = result.get();

    // then
    assertAll(
      () -> assertTrue(result.isBound()),
      () -> assertEquals(6379, config.port()),
      () -> assertEquals("localhost", config.host())
    );
  }
}