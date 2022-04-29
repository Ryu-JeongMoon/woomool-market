package com.woomoolmarket.config;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

  @Value("${mail.smtp.host}")
  private String host;
  @Value("${mail.smtp.port}")
  private int port;
  @Value("${mail.smtp.username}")
  private String username;
  @Value("${mail.smtp.password}")
  private String password;

  @Bean
  public JavaMailSenderImpl mailSender() {

    Properties prop = new Properties();
    prop.put("mail.smtp.auth", "true");
    prop.put("mail.smtp.debug", "true");
    prop.put("mail.smtp.starttls.enable", "true");
    prop.put("mail.smtp.EnableSSL.enable", "true");

    JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
    javaMailSender.setJavaMailProperties(prop);
    javaMailSender.setProtocol("smtp");
    javaMailSender.setUsername(username);
    javaMailSender.setPassword(password);
    javaMailSender.setHost(host);
    javaMailSender.setPort(port);
    return javaMailSender;
  }
}
