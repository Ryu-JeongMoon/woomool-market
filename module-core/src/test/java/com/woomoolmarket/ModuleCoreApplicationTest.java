package com.woomoolmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.woomoolmarket")
public class ModuleCoreApplicationTest {

  public static void main(String[] args) {
    SpringApplication.run(ModuleCoreApplicationTest.class, args);
  }
}