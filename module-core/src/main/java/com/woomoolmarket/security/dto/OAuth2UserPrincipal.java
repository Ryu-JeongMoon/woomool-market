package com.woomoolmarket.security.dto;

import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.entity.enumeration.Role;
import com.woomoolmarket.domain.entity.enumeration.Status;
import com.woomoolmarket.util.constants.ExceptionMessages;
import com.woomoolmarket.util.constants.TokenConstants;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;

@Getter
@ToString
public class OAuth2UserPrincipal extends UserPrincipal implements OAuth2User, OidcUser {

  private static final String NOT_VALID_TOKEN_VALUE = "NOT_VALID";

  private final OAuth2Token oAuth2Token;
  private final OidcIdToken oidcIdToken;
  private final Map<String, Object> claims;
  private final Map<String, Object> attributes;

  public OAuth2UserPrincipal(Long id, String email, Status status, OAuth2Token oAuth2Token, OidcIdToken oidcIdToken,
    Map<String, Object> claims, Map<String, Object> attributes, Collection<Role> authorities) {

    super(id, email, "", status, authorities);
    this.oAuth2Token = oAuth2Token;
    this.oidcIdToken = oidcIdToken;
    this.claims = claims;
    this.attributes = attributes;
  }

  public static OAuth2UserPrincipal from(Member member) {
    return new OAuth2UserPrincipal(
      member.getId(),
      member.getEmail(),
      member.getStatus(),
      Jwt.withTokenValue(NOT_VALID_TOKEN_VALUE)
        .header(TokenConstants.BEARER_TYPE, TokenConstants.BEARER_TYPE)
        .issuer(TokenConstants.GOOGLE_TOKEN_ISSUER)
        .build(),
      OidcIdToken.withTokenValue(NOT_VALID_TOKEN_VALUE)
        .issuer(TokenConstants.GOOGLE_TOKEN_ISSUER)
        .build(),
      Collections.emptyMap(),
      Collections.emptyMap(),
      Collections.singletonList(member.getRole())
    );
  }

  public static OAuth2UserPrincipal of(Member member, OAuth2User oAuth2User, OAuth2Token oAuth2Token) {
    return oAuth2User instanceof OidcUser
      ? toOidcUserPrincipal(member, (OidcUser) oAuth2User, oAuth2Token)
      : toOAuth2UserPrincipal(member, oAuth2User, oAuth2Token);
  }

  private static OAuth2UserPrincipal toOAuth2UserPrincipal(Member member, OAuth2User oAuth2User, OAuth2Token oAuth2Token) {
    return new OAuth2UserPrincipal(
      member.getId(),
      member.getEmail(),
      member.getStatus(),
      oAuth2Token,
      OidcIdToken.withTokenValue(NOT_VALID_TOKEN_VALUE)
        .issuer(TokenConstants.GOOGLE_TOKEN_ISSUER)
        .build(),
      Collections.emptyMap(),
      oAuth2User.getAttributes(),
      Collections.singletonList(member.getRole())
    );
  }

  private static OAuth2UserPrincipal toOidcUserPrincipal(Member member, OidcUser oidcUser, OAuth2Token oAuth2Token) {
    return new OAuth2UserPrincipal(
      member.getId(),
      member.getEmail(),
      member.getStatus(),
      oAuth2Token,
      oidcUser.getIdToken(),
      oidcUser.getClaims(),
      oidcUser.getAttributes(),
      Collections.singletonList(member.getRole())
    );
  }

  public Authentication toAuthentication() {
    return new UsernamePasswordAuthenticationToken(
      this,
      "",
      this.getAuthorities()
    );
  }

  @Override
  public Map<String, Object> getAttributes() {
    return Collections.unmodifiableMap(attributes);
  }

  @Override
  public Collection<Role> getAuthorities() {
    return Collections.unmodifiableCollection(super.getAuthorities());
  }

  @Override
  public String getName() {
    return getEmail();
  }

  @Override
  public Map<String, Object> getClaims() {
    return Collections.unmodifiableMap(claims);
  }

  @Override
  public OidcUserInfo getUserInfo() {
    throw new UnsupportedOperationException(ExceptionMessages.Common.UNSUPPORTED_OPERATION);
  }

  @Override
  public OidcIdToken getIdToken() {
    return oidcIdToken;
  }
}