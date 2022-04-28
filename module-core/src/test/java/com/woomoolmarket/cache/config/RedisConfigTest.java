package com.woomoolmarket.cache.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("classpath:application.yml")
class RedisConfigTest {

  @Autowired
  private RedisProperties redisProperties;

  @Test
  @DisplayName("application.yml 속성 반환")
  void envTest() {
    int port = redisProperties.getPort();
    String host = redisProperties.getHost();

    assertThat(port).isEqualTo(6379);
    assertThat(host).isEqualTo("localhost");
  }
}