package com.woomoolmarket.helper;

import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.entity.embeddable.Address;
import com.woomoolmarket.domain.entity.enumeration.Role;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MemberTestHelper {

  public static final String MEMBER_EMAIL = "panda@naver.com";
  public static final String MEMBER_PASSWORD = "123456";
  public static final String MEMBER_NICKNAME = "panda";
  public static final String MEMBER_PHONE = "01012345678";
  public static final Address MEMBER_ADDRESS = new Address("seoul", "yeonhui", "01023");

  private static final PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();

  public static Member createUser() {
    return Member.builder()
      .email(MEMBER_EMAIL)
      .nickname(MEMBER_NICKNAME)
      .role(Role.USER)
      .password(passwordEncoder.encode(MEMBER_PASSWORD))
      .address(MEMBER_ADDRESS)
      .phone(MEMBER_PHONE)
      .build();
  }

  public static Member createSeller() {
    return Member.builder()
      .email(MEMBER_EMAIL)
      .nickname(MEMBER_NICKNAME)
      .role(Role.SELLER)
      .password(passwordEncoder.encode(MEMBER_PASSWORD))
      .phone(MEMBER_PHONE)
      .address(MEMBER_ADDRESS)
      .build();
  }
}
