package com.woomoolmarket.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class TokenResponse {

  private String idToken;
  private String accessToken;
  private String refreshToken;
  private String grantType;
  private long accessTokenExpiresIn;
  private long refreshTokenExpiresIn;
}
