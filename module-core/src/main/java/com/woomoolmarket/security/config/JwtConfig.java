package com.woomoolmarket.security.config;

import com.woomoolmarket.security.filter.ApplicationAuthenticationFilter;
import com.woomoolmarket.security.filter.JwtAuthenticationEntryPoint;
import com.woomoolmarket.security.filter.OAuth2AuthenticationFilter;
import com.woomoolmarket.security.filter.OidcAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class JwtConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

  private final OidcAuthenticationFilter oidcAuthenticationFilter;
  private final OAuth2AuthenticationFilter oAuth2AuthenticationFilter;
  private final ApplicationAuthenticationFilter applicationAuthenticationFilter;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http
      .addFilterBefore(applicationAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
      .addFilterAfter(oAuth2AuthenticationFilter, ApplicationAuthenticationFilter.class)
      .addFilterAfter(oidcAuthenticationFilter, OAuth2AuthenticationFilter.class)
      .exceptionHandling()
      .authenticationEntryPoint(jwtAuthenticationEntryPoint);
  }
}
