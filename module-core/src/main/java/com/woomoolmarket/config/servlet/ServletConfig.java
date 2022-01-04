package com.woomoolmarket.config.servlet;

import io.undertow.servlet.api.SecurityConstraint;
import io.undertow.servlet.api.SecurityInfo;
import io.undertow.servlet.api.TransportGuaranteeType;
import io.undertow.servlet.api.WebResourceCollection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletConfig {

  @Value("${http.port:0}")
  private int httpPort;

  @Value("${server.port:0}")
  private int sslPort;

  @Bean
  public ServletWebServerFactory serverFactory() {
    UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();

    factory.addBuilderCustomizers(builder -> builder.addHttpListener(httpPort, "0.0.0.0"));

    factory.addDeploymentInfoCustomizers(deploymentInfo ->
      deploymentInfo.addSecurityConstraint(
          new SecurityConstraint()
            .addWebResourceCollection(new WebResourceCollection().addUrlPattern("/*"))
            .setTransportGuaranteeType(TransportGuaranteeType.CONFIDENTIAL)
            .setEmptyRoleSemantic(SecurityInfo.EmptyRoleSemantic.PERMIT))
        .setConfidentialPortManager(exchange -> sslPort));

    return factory;
  }
}
