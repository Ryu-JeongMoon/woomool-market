package com.woomoolmarket.security.config;

import com.woomoolmarket.security.oauth2.CustomOAuth2Provider;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(OAuth2Properties.class)
public class OAuth2Config {

  private final OAuth2Properties oAuth2Properties;

  @Bean
  public ClientRegistrationRepository clientRegistrationRepository() {
    List<ClientRegistration> registrations = oAuth2Properties.registration()
      .keySet()
      .stream()
      .map(this::getRegistration)
      .toList();

    return new InMemoryClientRegistrationRepository(registrations);
  }

  private ClientRegistration getRegistration(OAuth2Properties.Client client) {
    OAuth2Properties.Resource resource = oAuth2Properties.registration().get(client);
    String clientName = resource.clientName();

    return CustomOAuth2Provider.valueOfCaseInsensitively(clientName)
      .getBuilder(clientName)
      .clientId(resource.clientId())
      .clientSecret(resource.clientSecret())
      .build();
  }
}
