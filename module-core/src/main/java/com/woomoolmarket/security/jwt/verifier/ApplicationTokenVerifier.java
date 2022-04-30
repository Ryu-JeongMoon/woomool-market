package com.woomoolmarket.security.jwt.verifier;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nimbusds.jwt.SignedJWT;
import com.woomoolmarket.domain.entity.enumeration.Role;
import com.woomoolmarket.security.dto.UserPrincipal;
import com.woomoolmarket.util.TokenUtils;
import com.woomoolmarket.util.constants.TokenConstants;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;

public interface ApplicationTokenVerifier extends TokenVerifier {

  default DecodedJWT getDecodedJWT(String token) {
    try {
      return JWT.decode(token);
    } catch (JWTDecodeException e) {
      return TokenUtils.getEmptyDecodedJWT();
    }
  }

  default SignedJWT getSingedJWT(String token) throws ParseException {
    return SignedJWT.parse(token);
  }

  @Override
  default boolean isLocalToken(String idToken) {
    try {
      DecodedJWT decodedJWT = getDecodedJWT(idToken);
      return StringUtils.equals(TokenConstants.LOCAL_TOKEN_ISSUER, decodedJWT.getIssuer());
    } catch (JWTDecodeException | NullPointerException e) {
      return false;
    }
  }

  @Override
  @SneakyThrows
  default Authentication getAuthentication(String idToken) {
    DecodedJWT decodedJWT = getDecodedJWT(idToken);

    Long id = decodedJWT.getClaim(TokenConstants.USER_ID).asLong();
    String email = decodedJWT.getClaim(TokenConstants.EMAIL).asString();
    String authority = decodedJWT.getClaim(TokenConstants.AUTHORITIES).asString();
    List<Role> authorities = Collections.singletonList(Role.valueOfCaseInsensitively(authority));

    return UserPrincipal
      .of(id, email, "", authorities)
      .toAuthentication();
  }
}
