package com.woomoolmarket.security.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TokenResponse implements Serializable {

  private String grantType;

  private String accessToken;

  private String refreshToken;

  private long accessTokenExpiresIn;

  private long refreshTokenExpiresIn;
}
