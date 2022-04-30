package com.woomoolmarket.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class XssConfig implements WebMvcConfigurer {

  private final ObjectMapper objectMapper;

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(jsonEscapeConverter());
  }

  @Bean
  public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
    objectMapper.getFactory().setCharacterEscapes(new HTMLCharacterEscapes());
    return new MappingJackson2HttpMessageConverter(objectMapper);
  }

  @Bean
  public FilterRegistrationBean<XssEscapeServletFilter> filterRegistrationBean() {
    FilterRegistrationBean<XssEscapeServletFilter> filterRegistration = new FilterRegistrationBean<>();
    filterRegistration.addUrlPatterns("/*");
    filterRegistration.setFilter(new XssEscapeServletFilter());
    filterRegistration.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
    return filterRegistration;
  }
}
