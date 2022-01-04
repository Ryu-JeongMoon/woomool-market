package com.woomoolmarket.security.oauth2;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.function.BiFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OAuth2AttributesTest {

  private static OAuth2Attributes oAuth2Attributes;
  private static final String REGISTRATION_ID = "facebook";
  private static final String NAME_ATTRIBUTE_KEY = "panda-attribute-key";
  private static final String USERNAME_ATTRIBUTE_NAME = "panda-attribute-name";
  private static final String NICKNAME = "panda-nickname";
  private static final String EMAIL = "panda-email";
  private static final String PROFILE_IMAGE = "panda-image";
  private static final Map<String, BiFunction<String, Map<String, Object>, OAuth2Attributes>> attributesMap = Map.of(
    "facebook", OAuth2AttributesTest::ofFacebook);
  private static final Map<String, Object> ATTRIBUTES =
    Map.of("panda", "bear", "email", EMAIL, "name", NICKNAME, "profile_image", PROFILE_IMAGE);

  private static OAuth2Attributes ofFacebook(String userNameAttributeName, Map<String, Object> attributes) {
    return OAuth2Attributes.builder()
      .nickname((String) attributes.get("name"))
      .email((String) attributes.get("email"))
      .profileImage((String) attributes.get("picture"))
      .attributes(attributes)
      .nameAttributeKey(userNameAttributeName)
      .build();
  }

  @BeforeEach
  void init() {
    oAuth2Attributes = new OAuth2Attributes(ATTRIBUTES, NAME_ATTRIBUTE_KEY, NICKNAME, EMAIL, PROFILE_IMAGE);
  }

  @Test
  @DisplayName("of 통한 OAuth2Attributes 생성")
  void of() {
    OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(REGISTRATION_ID, USERNAME_ATTRIBUTE_NAME, ATTRIBUTES);
    assertThat(oAuth2Attributes.getAttributes().get("panda")).isEqualTo("bear");
  }

  @Test
  @DisplayName("Member 변환")
  void toEntity() {
    assertThat(oAuth2Attributes.toEntity().getEmail()).isEqualTo(EMAIL);
  }

  @Test
  @DisplayName("attributes 반환")
  void getAttributes() {
    assertThat(oAuth2Attributes.getAttributes().get("panda")).isEqualTo(ATTRIBUTES.get("panda"));
  }

  @Test
  @DisplayName("nameAttributeKey 반환")
  void getNameAttributeKey() {
    assertThat(oAuth2Attributes.getNameAttributeKey()).isEqualTo(NAME_ATTRIBUTE_KEY);
  }

  @Test
  @DisplayName("OAuth2Attributes 생성")
  void getNickname() {
    assertThat(oAuth2Attributes.getNickname()).isEqualTo(NICKNAME);
  }

  @Test
  @DisplayName("OAuth2Attributes 생성")
  void getEmail() {
    assertThat(oAuth2Attributes.getEmail()).isEqualTo(EMAIL);
  }

  @Test
  @DisplayName("OAuth2Attributes 생성")
  void getProfileImage() {
    assertThat(oAuth2Attributes.getProfileImage()).isEqualTo(PROFILE_IMAGE);
  }

  @Test
  @DisplayName("builder 를 통한 OAuth2Attributes 생성")
  void builder() {
    OAuth2Attributes oAuth2Attributes = OAuth2Attributes.builder()
      .email("yahoo")
      .build();
    assertThat(oAuth2Attributes.getEmail()).isNotNull();
    assertThat(oAuth2Attributes.getAttributes()).isNull();
    assertThat(oAuth2Attributes.getNickname()).isNull();
    assertThat(oAuth2Attributes.getProfileImage()).isNull();
  }
}