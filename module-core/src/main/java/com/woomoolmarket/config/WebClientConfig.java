package com.woomoolmarket.config;

import com.woomoolmarket.util.constants.UriConstants;
import com.woomoolmarket.util.wrapper.ThrowingConsumer;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Slf4j
@Configuration
public class WebClientConfig {

  private static final int DEFAULT_TIMEOUT_SECONDS = 5;
  private static final int MAX_BYTE_COUNT = 1024 * 1024 * 50;

  @Bean
  public WebClient webClient() {
    ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
      .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(MAX_BYTE_COUNT))
      .build();

    exchangeStrategies
      .messageWriters().stream()
      .filter(LoggingCodecSupport.class::isInstance)
      .forEach(writer -> ((LoggingCodecSupport) writer).setEnableLoggingRequestDetails(true));

    return WebClient.builder()
      .baseUrl(UriConstants.Full.BASE_URL)
      .clientConnector(
        new ReactorClientHttpConnector(
          HttpClient
            .create()
            .secure(
              ThrowingConsumer.unchecked(
                sslContextSpec -> sslContextSpec.sslContext(
                  SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build()
                )
              )
            )
            .responseTimeout(Duration.ofMillis(120_000))
            .doOnConnected(
              conn -> conn
                .addHandlerLast(new ReadTimeoutHandler(DEFAULT_TIMEOUT_SECONDS))
                .addHandlerLast(new WriteTimeoutHandler(DEFAULT_TIMEOUT_SECONDS))
            )
        )
      )
      .exchangeStrategies(exchangeStrategies)
      .filter(ExchangeFilterFunction.ofRequestProcessor(
        clientRequest -> {
          log.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
          clientRequest
            .headers()
            .forEach((name, values) -> values.forEach(value -> log.debug("{} : {}", name, value)));
          return Mono.just(clientRequest);
        }
      ))
      .filter(ExchangeFilterFunction.ofResponseProcessor(
        clientResponse -> {
          clientResponse
            .headers()
            .asHttpHeaders()
            .forEach((name, values) -> values.forEach(value -> log.debug("{} : {}", name, value)));
          return Mono.just(clientResponse);
        }
      ))
      .defaultHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36")
      .build();
  }
}