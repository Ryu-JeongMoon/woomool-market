package com.woomoolmarket.config.aws;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "cloud.aws")
public class AwsS3Properties {

  private final Region region;
  private final Credentials credentials;

  /**
   * aws region 정보는 cloud.aws.region.static 으로 구할 수 있다<br/>
   * static 은 자바의 reserved keyword 이므로 관례에 어긋나지만 Static 으로 받도록 한다
   */
  @Getter
  @ConstructorBinding
  @RequiredArgsConstructor
  static class Region {

    private final String Static;
  }

  @Getter
  @ConstructorBinding
  @RequiredArgsConstructor
  static class Credentials {

    private final String accessKey;
    private final String secretKey;
  }
}
