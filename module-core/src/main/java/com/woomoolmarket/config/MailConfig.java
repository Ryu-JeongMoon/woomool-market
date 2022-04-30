package com.woomoolmarket.config;

import com.woomoolmarket.config.properties.MailProperties;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(MailProperties.class)
public class MailConfig {

  private final MailProperties mailProperties;

  @Bean
  public JavaMailSenderImpl mailSender() {
    // todo, configuration-properties 속성 추가
    Properties prop = new Properties();
    prop.put("mail.smtp.auth", "true");
    prop.put("mail.smtp.debug", "true");
    prop.put("mail.smtp.starttls.enable", "true");
    prop.put("mail.smtp.EnableSSL.enable", "true");

    JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
    javaMailSender.setJavaMailProperties(prop);
    javaMailSender.setProtocol("smtp");
    javaMailSender.setHost(mailProperties.host());
    javaMailSender.setPort(mailProperties.port());
    javaMailSender.setUsername(mailProperties.username());
    javaMailSender.setPassword(mailProperties.password());
    return javaMailSender;
  }
}
