package com.woomoolmarket.config;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.woomoolmarket.config.properties.AwsS3Properties;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;

class AmazonS3ConfigTest {

  @Test
  @DisplayName("application.yml aws binding test")
  void awsS3Binding() {
    // given
    Map<String, String> properties = Map.of(
      "cloud.aws.credentials.access-key", "panda",
      "cloud.aws.credentials.secret-key", "bear",
      "cloud.aws.region.static", "panda"
    );

    // when
    ConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
    Binder binder = new Binder(source);
    BindResult<AwsS3Properties> result = binder.bind("cloud.aws", AwsS3Properties.class);
    AwsS3Properties config = result.get();

    // then
    assertAll(
      () -> assertTrue(result.isBound()),
      () -> assertEquals("panda", config.credentials().accessKey()),
      () -> assertEquals("bear", config.credentials().secretKey()),
      () -> assertEquals("panda", config.region().Static())
    );
  }
}