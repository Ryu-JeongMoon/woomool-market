package com.woomoolmarket.security.oauth2;

import com.woomoolmarket.domain.member.entity.Member;
import java.util.Map;
import java.util.function.BiFunction;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuth2Attributes {

  private static final Map<String, BiFunction<String, Map<String, Object>, OAuth2Attributes>> attributesByProvider = Map.of(
    "kakao", OAuth2Attributes::ofKakao,
    "naver", OAuth2Attributes::ofNaver,
    "google", OAuth2Attributes::ofGoogle,
    "github", OAuth2Attributes::ofGithub,
    "facebook", OAuth2Attributes::ofFacebook
  );

  private final String email;
  private final String nickname;
  private final String profileImage;
  private final String nameAttributeKey;
  private final Map<String, Object> attributes;

  @Builder
  public OAuth2Attributes(
    String email, String nickname, Map<String, Object> attributes, String profileImage, String nameAttributeKey) {
    this.email = email;
    this.nickname = nickname;
    this.attributes = attributes;
    this.profileImage = profileImage;
    this.nameAttributeKey = nameAttributeKey;
  }

  // switch 문이 이해하기 더 편하긴 한듯..?
  public static OAuth2Attributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
    return attributesByProvider.get(registrationId).apply(userNameAttributeName, attributes);
  }

  private static OAuth2Attributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
    return OAuth2Attributes.builder()
      .email((String) attributes.get("email"))
      .nickname((String) attributes.get("name"))
      .profileImage((String) attributes.get("picture"))
      .attributes(attributes)
      .nameAttributeKey(userNameAttributeName)
      .build();
  }

  private static OAuth2Attributes ofGithub(String userNameAttributeName, Map<String, Object> attributes) {
    return OAuth2Attributes.builder()
      .email((String) attributes.get("email"))
      .nickname((String) attributes.get("name"))
      .profileImage((String) attributes.get("avatar_url"))
      .attributes(attributes)
      .nameAttributeKey(userNameAttributeName)
      .build();
  }

  private static OAuth2Attributes ofFacebook(String userNameAttributeName, Map<String, Object> attributes) {
    return OAuth2Attributes.builder()
      .email((String) attributes.get("email"))
      .nickname((String) attributes.get("name"))
      .profileImage((String) attributes.get("picture"))
      .attributes(attributes)
      .nameAttributeKey(userNameAttributeName)
      .build();
  }

  private static OAuth2Attributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
    /* naver는 user_name_attribute -> response 로 받아옴, 다른 곳들은 id */
    Map<String, Object> response = (Map<String, Object>) attributes.get("response");

    return OAuth2Attributes.builder()
      .email((String) response.get("email"))
      .nickname((String) response.get("name"))
      .profileImage((String) response.get("profile_image"))
      .attributes(attributes)
      .nameAttributeKey(userNameAttributeName)
      .build();
  }

  private static OAuth2Attributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
    /* email -> kakaoAccount에 있고, nickname & profile_image_url -> kakaoProfile에 있음 */

    Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

    return OAuth2Attributes.builder()
      .email((String) kakaoAccount.get("email"))
      .nickname((String) kakaoProfile.get("nickname"))
      .profileImage((String) kakaoProfile.get("profile_image_url"))
      .attributes(attributes)
      .nameAttributeKey(userNameAttributeName)
      .build();
  }

  public Member toEntity() {
    return Member.builder()
      .email(email)
      .nickname(nickname)
      .profileImage(profileImage)
      .build();
  }
}
