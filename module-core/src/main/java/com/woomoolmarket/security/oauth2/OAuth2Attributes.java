package com.woomoolmarket.security.oauth2;

import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.entity.enumeration.AuthProvider;
import java.util.Map;
import java.util.function.Function;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

@Getter
@Builder
@RequiredArgsConstructor
public class OAuth2Attributes {

  // todo, enum 방식 변경
  private static final Map<String, Function<Map<String, Object>, OAuth2Attributes>> attributesByProvider = Map.of(
    "kakao", OAuth2Attributes::ofKakao,
    "naver", OAuth2Attributes::ofNaver,
    "google", OAuth2Attributes::ofGoogle,
    "github", OAuth2Attributes::ofGithub,
    "facebook", OAuth2Attributes::ofFacebook
  );
  private static Member member;
  private final String email;
  private final String nickname;
  private final String profileImage;
  private final Map<String, Object> attributes;

  public static OAuth2Attributes of(String registrationId, Map<String, Object> attributes) {
    return attributesByProvider.get(registrationId).apply(attributes);
  }

  private static OAuth2Attributes ofGoogle(Map<String, Object> attributes) {
    return OAuth2Attributes.builder()
      .email((String) attributes.get("email"))
      .nickname((String) attributes.get("name"))
      .profileImage((String) attributes.get("picture"))
      .attributes(attributes)
      .build();
  }

  private static OAuth2Attributes ofGithub(Map<String, Object> attributes) {
    return OAuth2Attributes.builder()
      .email((String) attributes.get("email"))
      .nickname((String) attributes.get("name"))
      .profileImage((String) attributes.get("avatar_url"))
      .attributes(attributes)
      .build();
  }

  private static OAuth2Attributes ofFacebook(Map<String, Object> attributes) {
    return OAuth2Attributes.builder()
      .email((String) attributes.get("email"))
      .nickname((String) attributes.get("name"))
      .profileImage((String) attributes.get("picture"))
      .attributes(attributes)
      .build();
  }

  @SuppressWarnings("unchecked")
  private static OAuth2Attributes ofNaver(Map<String, Object> attributes) {
    /* naver는 user_name_attribute -> response 로 받아옴, 다른 곳들은 id */
    Map<String, Object> response = (Map<String, Object>) attributes.get("response");

    return OAuth2Attributes.builder()
      .email((String) response.get("email"))
      .nickname((String) response.get("name"))
      .profileImage((String) response.get("profile_image"))
      .attributes(attributes)
      .build();
  }

  @SuppressWarnings("unchecked")
  private static OAuth2Attributes ofKakao(Map<String, Object> attributes) {
    /* email -> kakaoAccount에 있고, nickname & profile_image_url -> kakaoProfile에 있음 */
    Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

    return OAuth2Attributes.builder()
      .email((String) kakaoAccount.get("email"))
      .nickname((String) kakaoProfile.get("nickname"))
      .profileImage((String) kakaoProfile.get("profile_image_url"))
      .attributes(attributes)
      .build();
  }

  public Member toMember(String registrationId) {
    if (member != null)
      return member;

    Argon2PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();
    String defaultRandomPassword = RandomStringUtils.randomAlphanumeric(10);
    String encodedRandomPassword = passwordEncoder.encode(defaultRandomPassword);
    AuthProvider initialAuthProvider = AuthProvider.valueOfCaseInsensitively(registrationId);

    member = Member.builder()
      .email(email)
      .nickname(nickname)
      .password(encodedRandomPassword)
      .initialAuthProvider(initialAuthProvider)
      .build();
    return member;
  }
}
