package com.woomoolmarket.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "cloud.aws")
public record AwsS3Properties(Region region, Credentials credentials) {

  /**
   * aws region 정보는 cloud.aws.region.static 으로 구할 수 있다<br/>
   * static 은 자바의 reserved keyword 이므로 관례에 어긋나지만 Static 으로 받도록 한다
   */
  @ConstructorBinding
  public record Region(String Static) {

  }

  @ConstructorBinding
  public record Credentials(String accessKey, String secretKey) {

  }
}
