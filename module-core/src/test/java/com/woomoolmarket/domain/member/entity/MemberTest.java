package com.woomoolmarket.domain.member.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.image.entity.Image;
import com.woomoolmarket.helper.ImageTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class MemberTest {

  private static final String EMAIL = "panda@naver.com";
  private static final String NICKNAME = "panda";
  private static final String RAW_PASSWORD = "1592";
  private static final String PROFILE_IMAGE = "bear";

  private final PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();

  private Member member;

  @BeforeEach
  void setUp() {
    member = Member.builder()
      .email(EMAIL)
      .nickname(NICKNAME)
      .profileImage(PROFILE_IMAGE)
      .password(passwordEncoder.encode(RAW_PASSWORD))
      .build();
  }

  @Test
  @DisplayName("비밀번호 변경 성공")
  void changePasswordTest() {
    String newPassword = "1234";
    String newEncodedPassword = passwordEncoder.encode(newPassword);
    member.changePassword(newEncodedPassword);

    passwordEncoder.matches(newPassword, member.getPassword());
  }

  @Test
  @DisplayName("Status 기본 상태 ACTIVE 초기화")
  void initialStatusTest() {
    assertThat(member.getStatus()).isEqualTo(Status.ACTIVE);
  }

  @Test
  @DisplayName("Authority 기본 상태 ROLE_USER 초기화")
  void initialAuthorityTest() {
    assertThat(member.getAuthority()).isEqualTo(Authority.ROLE_USER);
  }

  @Test
  @DisplayName("AuthProvider 기본 상태 Local")
  void initialAuthProvider() {
      assertThat(member.getAuthProvider()).isEqualTo(AuthProvider.LOCAL);
  }

  @Test
  @DisplayName("Authority 변경 성공")
  void assignAuthorityTest() {
    member.assignAuthority(Authority.ROLE_ADMIN);
    assertThat(member.getAuthority()).isEqualTo(Authority.ROLE_ADMIN);
  }

  @Test
  @DisplayName("getAuthorityKey() 정상 작동")
  void getAuthority() {
    assertThat(member.getAuthorityKey()).isEqualTo(Authority.ROLE_USER.toString());
  }

  @Test
  @DisplayName("잘못된 비교 - USER & ADMIN")
  void getAuthorityFailByWrongAuthority() {
    assertThat(member.getAuthorityKey()).isNotEqualTo(Authority.ROLE_ADMIN.toString());
  }

  @Test
  @DisplayName("leave() 정상 작동 ~!")
  void leaveTest() {
    member.leave();

    assertThat(member.getStatus()).isEqualTo(Status.INACTIVE);
    assertThat(member.getLeaveDateTime()).isNotNull();
  }

  @Test
  @DisplayName("restore() 정상 작동 ~!")
  void restoreTest() {
    member.leave();
    member.restore();

    assertThat(member.getStatus()).isEqualTo(Status.ACTIVE);
    assertThat(member.getLeaveDateTime()).isNull();
  }

  @Test
  @DisplayName("SOCIAL 로그인 사용자 정보 수정")
  void editNicknameAndProfileTest() {
    Member result = member.editByOAuth2("white", "tiger", AuthProvider.FACEBOOK);

    assertThat(member.getNickname()).isEqualTo(result.getNickname());
    assertThat(member.getProfileImage()).isEqualTo(result.getProfileImage());
    assertThat(member.getAuthProvider()).isEqualTo(AuthProvider.FACEBOOK);
  }

  @Test
  @DisplayName("기본 비밀번호 - 암호화된 비밀번호 비교 실패")
  void rawPasswordComparisonTest() {
    assertThat(RAW_PASSWORD).isNotEqualTo(member.getPassword());
  }

  @Test
  @DisplayName("암호화된 비밀번호 비교 성공")
  void encodedPasswordComparisonTest() {
    assertTrue(passwordEncoder.matches(RAW_PASSWORD, member.getPassword()));
  }

  @Test
  @DisplayName("이미지 저장 성공")
  void addImageTest() {
    Image image = ImageTestHelper.createImage();
    member.addImage(image);

    assertThat(member.getImage()).isNotNull();
  }

  @Test
  @DisplayName("equals & hashCode 테스트")
  void equalsAndHashCodeTest() {
    Member newMember = Member.builder()
      .email(EMAIL)
      .build();

    assertThat(newMember.equals(member)).isTrue();
    assertThat(newMember.hashCode()).isEqualTo(member.hashCode());
  }
}