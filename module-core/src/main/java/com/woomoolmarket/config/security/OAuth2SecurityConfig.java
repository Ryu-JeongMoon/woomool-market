package com.woomoolmarket.config.security;

import com.woomoolmarket.security.oauth2.CustomOAuth2Provider;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

@Configuration
@RequiredArgsConstructor
public class OAuth2SecurityConfig {

  private final static List<String> CLIENTS = List.of("google", "facebook", "naver", "kakao", "github");
  private final static String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";

  private final Environment env;

  @Bean
  public ClientRegistrationRepository clientRegistrationRepository() {
    List<ClientRegistration> registrations = CLIENTS.stream()
      .map(this::getRegistration)
      .collect(Collectors.toList());

    return new InMemoryClientRegistrationRepository(registrations);
  }

  private ClientRegistration getRegistration(String client) {
    String clientId = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-id");
    String clientSecret = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-secret");

    return CustomOAuth2Provider.valueOf(client.toUpperCase())
      .getBuilder(client)
      .clientId(clientId)
      .clientSecret(clientSecret)
      .build();
  }
}
