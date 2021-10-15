package com.woomoolmarket.config.aws;

import static org.junit.jupiter.api.Assertions.*;

import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

@Log4j2
@SpringBootTest
class AmazonS3ConfigTest {

    @Autowired
    Environment env;

    @Test
    void configureTest() {
        String region = env.getProperty("cloud.aws.region.static");
        Assertions.assertThat(region).isEqualTo("ap-northeast-2");
    }
}