package com.woomoolmarket.config.aws;

import static org.assertj.core.api.Assertions.*;

import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("yml 에서 올바른 정보 읽어온다")
    void configureTest() {
        String region = env.getProperty("cloud.aws.region.static");
        assertThat(region).isEqualTo("ap-northeast-2");
    }
}