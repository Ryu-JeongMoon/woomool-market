package com.woomoolmarket.security.jwt.factory;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;
import com.woomoolmarket.security.dto.TokenResponse;
import com.woomoolmarket.util.constants.Times;
import com.woomoolmarket.util.constants.TokenConstants;
import java.util.Date;
import org.springframework.security.core.Authentication;

public abstract class TokenFactory {

  public TokenResponse create(Authentication authentication) {
    String idToken = createIdToken(authentication);
    String accessToken = createAccessToken();
    String refreshToken = createRefreshToken(authentication);

    return TokenResponse.builder()
      .idToken(idToken)
      .accessToken(accessToken)
      .refreshToken(refreshToken)
      .build();
  }

  public TokenResponse renew(Authentication authentication, String refreshToken) {
    String idToken = createIdToken(authentication);
    String accessToken = createAccessToken();

    return TokenResponse.builder()
      .idToken(idToken)
      .accessToken(accessToken)
      .refreshToken(refreshToken)
      .build();
  }

  protected abstract String createIdToken(Authentication authentication);

  // todo, 매번 다른 값 나오는지 테스트 해볼 것
  private String createAccessToken() {
    try {
      String ellipticCurvePoint = new ECKeyGenerator(Curve.P_256)
        .keyUse(KeyUse.ENCRYPTION)
        .generate()
        .getX()
        .toString();

      return TokenConstants.ACCESS_TOKEN_PREFIX + ellipticCurvePoint;
    } catch (JOSEException e) {
      throw new IllegalStateException(e);
    }
  }

  ;

  protected abstract String createRefreshToken(Authentication authentication);

  /**
   * 만료 시간은 현재 시간부터 만료 시간을 더함으로써 정해진다<br/>
   * 토큰 생성을 위한 nimbus jwt 에서는 Date 로 계산하기 때문에<br/>
   * millis 단위의 계산을 수행 후 이를 인자로 새로운 Date 인스턴스를 만들어 반환한다<br/>
   */
  protected Date createExpirationDate(Times expirationInSeconds) {
    long currentTimeInMillis = new Date().getTime();
    long expirationTimeInMillis = Times.toMillis(expirationInSeconds);
    return new Date(currentTimeInMillis + expirationTimeInMillis);
  }
}
