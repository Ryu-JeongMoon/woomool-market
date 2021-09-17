package com.woomoolmarket.security.oauth2.user;

import com.woomoolmarket.security.exception.OAuth2AuthenticationProcessingException;
import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes)
        throws OAuth2AuthenticationProcessingException {

        switch (registrationId.toUpperCase()) {
            case "GOOGLE":
                return new GoogleOAuth2UserInfo(attributes);
            case "FACEBOOK":
                return new FacebookOAuth2UserInfo(attributes);
            case "NAVER":
                return new NaverOAuth2UserInfo(attributes);
            case "KAKAO":
                return new KakaoOAuth2UserInfo(attributes);
            default:
                throw new OAuth2AuthenticationProcessingException(
                    "Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}