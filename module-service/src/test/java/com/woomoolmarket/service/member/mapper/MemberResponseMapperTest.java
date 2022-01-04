package com.woomoolmarket.service.member.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberResponseMapperTest {

  private final MemberResponseMapperImpl memberResponseMapper = new MemberResponseMapperImpl();

  @Test
  @DisplayName("mapping 올바르게 작동")
  void memberMapperTest() {
    Member member = Member.builder()
      .email("panda@gmail.com")
      .nickname("panda")
      .password("123456")
      .build();

    MemberResponse memberResponse = memberResponseMapper.toDto(member);
    assertThat(memberResponse.getEmail()).isEqualTo(member.getEmail());
    assertThat(memberResponse.getNickname()).isEqualTo(member.getNickname());
    assertThat(memberResponse.getAddress()).isNull();
  }
}