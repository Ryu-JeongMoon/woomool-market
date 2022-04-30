package com.woomoolmarket.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.woomoolmarket.util.constants.TokenConstants;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth2TokenResponse {

  private String scope;

  @JsonProperty(TokenConstants.TOKEN_TYPE)
  private String tokenType;

  @JsonProperty(TokenConstants.EXPIRES_IN)
  private String expiresIn;

  @JsonProperty(TokenConstants.ID_TOKEN)
  private String oidcIdToken;

  @JsonProperty(TokenConstants.ACCESS_TOKEN)
  private String accessToken;

  @JsonProperty(TokenConstants.REFRESH_TOKEN)
  private String refreshToken;

  @Builder
  public OAuth2TokenResponse(String scope, String tokenType, String expiresIn, String oidcIdToken, String accessToken, String refreshToken) {
    this.scope = scope;
    this.tokenType = tokenType;
    this.expiresIn = expiresIn;
    this.oidcIdToken = oidcIdToken;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  public static OAuth2TokenResponse empty() {
    return new OAuth2TokenResponse();
  }
}
