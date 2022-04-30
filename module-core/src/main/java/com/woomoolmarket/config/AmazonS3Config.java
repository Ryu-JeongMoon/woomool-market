package com.woomoolmarket.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.woomoolmarket.config.properties.AwsS3Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AwsS3Properties.class)
public class AmazonS3Config {

  private final AwsS3Properties awsS3Properties;

  @Bean
  public AmazonS3Client amazonS3Client() {
    BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
      awsS3Properties.credentials().accessKey(),
      awsS3Properties.credentials().secretKey()
    );

    return (AmazonS3Client) AmazonS3ClientBuilder.standard()
      .withRegion(awsS3Properties.region().Static())
      .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
      .build();
  }
}
