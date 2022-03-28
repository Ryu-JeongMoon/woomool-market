package com.woomoolmarket.config.aws;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.amazonaws.services.s3.AmazonS3Client;
import com.woomoolmarket.config.aws.AwsS3Properties.Credentials;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AmazonS3ConfigTest {

  @Autowired
  AmazonS3Client amazonS3Client;
  @Autowired
  AwsS3Properties awsS3Properties;

  @Test
  @DisplayName("properties binding 성공")
  void awsS3Properties() {
    String region = awsS3Properties.getRegion().getStatic();
    Credentials credentials = awsS3Properties.getCredentials();

    assertAll(
      () -> assertThat(region).isEqualTo("ap-northeast-2"),
      () -> assertThat(credentials.getAccessKey()).isNotBlank(),
      () -> assertThat(credentials.getSecretKey()).isNotBlank()
    );

  }

  @Test
  @DisplayName("AmazonS3Client 설정 성공")
  void amazonS3Client() {
    assertNotNull(amazonS3Client);
  }
}