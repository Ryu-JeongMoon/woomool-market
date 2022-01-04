package com.woomoolmarket.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenConstants {

  public static final String REFRESH_TOKEN = "refreshToken";
  public static final String BEARER_TYPE = "Bearer ";
  public static final String AUTHORITIES_KEY = "auth";
  public static final String AUTHORIZATION_HEADER = "Authorization";

  public static final int ACCESS_TOKEN_EXPIRE_SECONDS = 60 * 30;
  public static final int REFRESH_TOKEN_EXPIRE_SECONDS = 60 * 60 * 24 * 7;

  public static final long ACCESS_TOKEN_EXPIRE_MILLIS = 1000 * 60 * 30;
  public static final long REFRESH_TOKEN_EXPIRE_MILLIS = 1000 * 60 * 60 * 24 * 7;

}
