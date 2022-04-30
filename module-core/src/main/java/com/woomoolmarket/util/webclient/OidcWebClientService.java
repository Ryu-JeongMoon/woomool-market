package com.woomoolmarket.util.webclient;

import com.woomoolmarket.security.dto.OAuth2TokenRequest;
import com.woomoolmarket.security.dto.OAuth2TokenResponse;
import com.woomoolmarket.util.constants.TokenConstants;
import com.woomoolmarket.util.constants.UriConstants;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Primary
@Service
@RequiredArgsConstructor
public class OidcWebClientService implements OidcWebClientWrapper {

  private final WebClient webClient;

  @Override
  public boolean validateByOidc(String idToken) {
    return webClient.mutate().baseUrl(UriConstants.Full.VALIDATION_URI).build()
      .get()
      .uri(uriBuilder -> uriBuilder
        .queryParam(TokenConstants.ID_TOKEN, idToken)
        .build())
      .retrieve()
      .onStatus(
        status -> status.is4xxClientError() || status.is5xxServerError(),
        this::convertToOAuth2AuthenticationException
      )
      .bodyToMono(String.class)
      .map(response -> !response.contains(TokenConstants.ERROR))
      .onErrorReturn(OAuth2AuthenticationException.class, false)
      .flux()
      .toStream()
      .findAny()
      .orElseGet(() -> false);
  }

  @Override
  public OAuth2TokenResponse getToken(OAuth2TokenRequest oAuth2TokenRequest) {
    return webClient.mutate().baseUrl(UriConstants.Full.TOKEN_REQUEST_URI).build()
      .post()
      .headers(this::configureDefaultHeaders)
      .bodyValue(oAuth2TokenRequest.toFormData())
      .retrieve()
      .onStatus(
        status -> status.is4xxClientError() || status.is5xxServerError(),
        this::convertToOAuth2AuthenticationException
      )
      .bodyToMono(OAuth2TokenResponse.class)
      .onErrorReturn(OAuth2TokenResponse.empty())
      .flux()
      .toStream()
      .findAny()
      .orElseGet(OAuth2TokenResponse::empty);
  }

  private void configureDefaultHeaders(HttpHeaders httpHeaders) {
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    httpHeaders.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
  }

  private Mono<OAuth2AuthenticationException> convertToOAuth2AuthenticationException(ClientResponse clientResponse) {
    return clientResponse
      .bodyToMono(String.class)
      .map(OAuth2AuthenticationException::new);
  }

  @Override
  public OAuth2TokenResponse getRenewedToken(OAuth2TokenRequest oAuth2TokenRequest) {
    return webClient.mutate().baseUrl(UriConstants.Full.TOKEN_REQUEST_URI).build()
      .post()
      .headers(this::configureDefaultHeaders)
      .bodyValue(oAuth2TokenRequest.toFormData())
      .retrieve()
      .onStatus(
        status -> status.is4xxClientError() || status.is5xxServerError(),
        this::convertToOAuth2AuthenticationException
      )
      .bodyToMono(OAuth2TokenResponse.class)
      .onErrorReturn(OAuth2TokenResponse.empty())
      .flux()
      .toStream()
      .findAny()
      .orElseGet(OAuth2TokenResponse::empty);
  }
}
