package com.woomoolmarket.config;

import com.woomoolmarket.config.properties.PortProperties;
import io.undertow.servlet.api.SecurityConstraint;
import io.undertow.servlet.api.SecurityInfo;
import io.undertow.servlet.api.TransportGuaranteeType;
import io.undertow.servlet.api.WebResourceCollection;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(PortProperties.class)
public class ServletConfig {

  private final PortProperties portProperties;

  @Bean
  public ServletWebServerFactory serverFactory() {
    UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();

    factory.addBuilderCustomizers(builder -> builder.addHttpListener(portProperties.httpPort(), "0.0.0.0"));

    factory.addDeploymentInfoCustomizers(deploymentInfo ->
      deploymentInfo.addSecurityConstraint(
          new SecurityConstraint()
            .addWebResourceCollection(new WebResourceCollection().addUrlPattern("/*"))
            .setTransportGuaranteeType(TransportGuaranteeType.CONFIDENTIAL)
            .setEmptyRoleSemantic(SecurityInfo.EmptyRoleSemantic.PERMIT))
        .setConfidentialPortManager(exchange -> portProperties.sslPort()));

    return factory;
  }
}
