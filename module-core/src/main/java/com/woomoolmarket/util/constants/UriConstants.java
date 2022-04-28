package com.woomoolmarket.util.constants;

public class UriConstants {

  public static final String[] SHOULD_NOT_FILTER_URL_PATTERNS = new String[]{
    "/css/**", "/js/**", "/vendor/**", "/webjars/**", "/img/**",
    "/logout", "/swagger-ui/**", "/swagger-resources/**", "/v2/**"
  };
}
