package com.woomoolmarket.domain.repository.querydto;

import com.woomoolmarket.domain.entity.RefreshToken;
import com.woomoolmarket.domain.entity.enumeration.AuthProvider;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshTokenQueryResponse {

  private String id;

  private Long memberId;

  private String tokenValue;

  private AuthProvider authProvider;

  @Builder
  public RefreshTokenQueryResponse(String id, Long memberId, String tokenValue, AuthProvider authProvider) {
    this.id = id;
    this.memberId = memberId;
    this.tokenValue = tokenValue;
    this.authProvider = authProvider;
  }

  public static RefreshTokenQueryResponse from(RefreshToken refreshToken) {
    return RefreshTokenQueryResponse.builder()
      .id(refreshToken.getId())
      .memberId(refreshToken.getMember().getId())
      .tokenValue(refreshToken.getTokenValue())
      .authProvider(refreshToken.getAuthProvider())
      .build();
  }
}
