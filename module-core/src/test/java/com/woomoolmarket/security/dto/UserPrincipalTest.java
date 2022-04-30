package com.woomoolmarket.security.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.config.TestConfig;
import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.entity.enumeration.Role;
import com.woomoolmarket.domain.repository.MemberRepository;
import java.util.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@DataJpaTest
@Import(TestConfig.class)
class UserPrincipalTest {

  private static final String MEMBER_EMAIL = "panda@naver.com";
  private static final String MEMBER_PASSWORD = "123456";
  private static final String MEMBER_NICKNAME = "panda";
  private static UserPrincipal principal;

  @Autowired
  MemberRepository memberRepository;
  PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();

  @BeforeEach
  void init() {
    Member member = Member.builder()
      .email(MEMBER_EMAIL)
      .password(passwordEncoder.encode(MEMBER_PASSWORD))
      .nickname(MEMBER_NICKNAME)
      .role(Role.USER)
      .build();
    memberRepository.save(member).getId();

    principal = UserPrincipal.from(member);
  }

  @Test
  @DisplayName("of 로 UserPrincipal 생성")
  void of() {
    assertThat(principal).isNotNull();
  }

  @Test
  @DisplayName("UserPrincipal 에서 회원 이메일 반환")
  void getUsername() {
    assertThat(principal.getUsername()).isEqualTo(MEMBER_EMAIL);
  }

  @Test
  @DisplayName("UserPrincipal 에서 회원 이메일 반환")
  void getName() {
    assertThat(principal.getEmail()).isEqualTo(MEMBER_EMAIL);
  }

  @Test
  @DisplayName("UserPrincipal 에서 권한 반환")
  void getAuthorities() {
    Collection<Role> authorities = principal.getAuthorities();
    assertThat(authorities.size()).isEqualTo(1);
  }

  @Test
  @DisplayName("계정 유효성 확인")
  void isAccountNonExpired() {
    assertThat(principal.isAccountNonExpired()).isTrue();
  }

  @Test
  @DisplayName("계정 유효성 확인")
  void isAccountNonLocked() {
    assertThat(principal.isAccountNonLocked()).isTrue();
  }

  @Test
  @DisplayName("계정 유효성 확인")
  void isCredentialsNonExpired() {
    assertThat(principal.isCredentialsNonExpired()).isTrue();
  }

  @Test
  @DisplayName("계정 유효성 확인")
  void isEnabled() {
    assertThat(principal.isEnabled()).isTrue();
  }
}