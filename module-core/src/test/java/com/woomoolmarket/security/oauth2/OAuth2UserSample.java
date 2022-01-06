package com.woomoolmarket.security.oauth2;

import com.woomoolmarket.domain.member.entity.Authority;
import java.util.Map;
import java.util.Set;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class OAuth2UserSample {

  public static final OAuth2User GOOGLE_USER = new DefaultOAuth2User(
    Set.of(new SimpleGrantedAuthority(Authority.ROLE_USER.getKey())),
    Map.of(
      "name", "PANDA",
      "sub", "113976141374150070219",
      "picture", "https://lh3.googleusercontent.com/a-/AOh14GgFLv4rMtdDUyBFDgsJggHdCK5IuKSLuOq9OwwLDyc=s96-c",
      "email", "panda@gmail.com"
    ), "sub");


  public static final OAuth2User FACEBOOK_USER = new DefaultOAuth2User(
    Set.of(new SimpleGrantedAuthority(Authority.ROLE_USER.getKey())),
    Map.of(
      "id", "4000026893357972",
      "name", "FACEBOOK_PANDA",
      "email", "panda@gmail.com"
    ), "id");

  public static final OAuth2User NAVER_USER = new DefaultOAuth2User(
    Set.of(new SimpleGrantedAuthority(Authority.ROLE_USER.getKey())),
    Map.of(
      "response", Map.of(
        "id", "18997705",
        "nickname", "NAVER_PANDA",
        "profile_image", "https://phinf.pstatic.net/contact/20180308_276/1520490317846up6kA_PNG/avatar_profile.png",
        "email", "panda@gmail.com",
        "name", "팬더"
      )
    ), "response");

  public static final OAuth2User KAKAO_USER = new DefaultOAuth2User(
    Set.of(new SimpleGrantedAuthority(Authority.ROLE_USER.getKey())),
    Map.of(
      "id", 1534230750,
      "kakao_account", Map.of(
        "profile", Map.of(
          "nickname", "KAKAO_PANDA",
          "thumbnail_image_url", "https://k.kakaocdn.net/dn/XQHgC/btqyj3C5jCQ/KjiijMK462WPrRrnkoOtY0/img_110x110.jpg",
          "profile_image_url", "https://k.kakaocdn.net/dn/XQHgC/btqyj3C5jCQ/KjiijMK462WPrRrnkoOtY0/img_640x640.jpg"
        ),
        "email", "panda@gmail.com"
      )
    ), "id");
}