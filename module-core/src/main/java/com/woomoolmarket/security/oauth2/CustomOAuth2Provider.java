package com.woomoolmarket.security.oauth2;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistration.Builder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;

public enum CustomOAuth2Provider {

  GOOGLE {
    @Override
    public Builder getBuilder(String registrationId) {
      ClientRegistration.Builder builder = getBuilder(registrationId, ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
      builder.scope("openid", "profile", "email");
      builder.authorizationUri("https://accounts.google.com/o/oauth2/v2/auth");
      builder.tokenUri("https://www.googleapis.com/oauth2/v4/token");
      builder.jwkSetUri("https://www.googleapis.com/oauth2/v3/certs");
      builder.issuerUri("https://accounts.google.com");
      builder.userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo");
      builder.userNameAttributeName(IdTokenClaimNames.SUB);
      builder.clientName("Google");
      return builder;
    }
  },

  GITHUB {
    @Override
    public Builder getBuilder(String registrationId) {
      ClientRegistration.Builder builder = getBuilder(registrationId, ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
      builder.scope("read:user", "user:email");
      builder.authorizationUri("https://github.com/login/oauth/authorize");
      builder.tokenUri("https://github.com/login/oauth/access_token");
      builder.userInfoUri("https://api.github.com/user");
      builder.userNameAttributeName("id");
      builder.clientName("GitHub");
      return builder;
    }
  },

  FACEBOOK {
    @Override
    public Builder getBuilder(String registrationId) {
      ClientRegistration.Builder builder = getBuilder(registrationId, ClientAuthenticationMethod.CLIENT_SECRET_POST);
      builder.scope("public_profile", "email");
      builder.authorizationUri("https://www.facebook.com/v2.8/dialog/oauth");
      builder.tokenUri("https://graph.facebook.com/v2.8/oauth/access_token");
      builder.userInfoUri("https://graph.facebook.com/me?fields=id,name,email");
      builder.userNameAttributeName("id");
      builder.clientName("Facebook");
      return builder;
    }
  },

  KAKAO {
    @Override
    public Builder getBuilder(String registrationId) {
      ClientRegistration.Builder builder = getBuilder(registrationId, ClientAuthenticationMethod.CLIENT_SECRET_POST);
      builder.scope("profile_image", "profile_nickname", "account_email");
      builder.authorizationUri("https://kauth.kakao.com/oauth/authorize");
      builder.tokenUri("https://kauth.kakao.com/oauth/token");
      builder.userInfoUri("https://kapi.kakao.com/v2/user/me");
      builder.userNameAttributeName("id");
      builder.clientName("Kakao");
      return builder;
    }
  },

  NAVER {
    @Override
    public Builder getBuilder(String registrationId) {
      ClientRegistration.Builder builder = getBuilder(registrationId, ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
      builder.scope("name", "email", "profile_image");
      builder.authorizationUri("https://nid.naver.com/oauth2.0/authorize");
      builder.tokenUri("https://nid.naver.com/oauth2.0/token");
      builder.userInfoUri("https://openapi.naver.com/v1/nid/me");
      builder.userNameAttributeName("response");
      builder.clientName("Naver");
      return builder;
    }
  };

  private static final String DEFAULT_REDIRECT_URL = "{baseUrl}/{action}/oauth2/code/{registrationId}";

  protected final ClientRegistration.Builder getBuilder(String registrationId, ClientAuthenticationMethod method) {
    ClientRegistration.Builder builder = ClientRegistration.withRegistrationId(registrationId);
    builder.clientAuthenticationMethod(method);
    builder.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE);
    builder.redirectUri(DEFAULT_REDIRECT_URL);
    return builder;
  }

  public abstract ClientRegistration.Builder getBuilder(String registrationId);

}
