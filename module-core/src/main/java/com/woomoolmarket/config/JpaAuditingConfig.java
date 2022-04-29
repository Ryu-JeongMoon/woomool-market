package com.woomoolmarket.config;

import java.util.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {

  @Bean
  public AuditorAware<String> auditorProvider() {
    return () -> {
      SecurityContext context = SecurityContextHolder.getContext();
      return context.getAuthentication() != null ? Optional.of(context.getAuthentication().getName()) : Optional.empty();
    };
  }
}
