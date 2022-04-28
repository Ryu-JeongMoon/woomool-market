package com.woomoolmarket.dto.member.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.woomoolmarket.domain.embeddable.Address;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.service.member.dto.request.SignupRequest;
import com.woomoolmarket.service.member.mapper.SignupRequestMapperImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Log4j2
class SingUpMapperTest {

  private final SignupRequestMapperImpl signUpMemberRequestMapper = new SignupRequestMapperImpl();

  @Test
  @DisplayName("signUpMapper 올바르게 변환된다")
  void signUpMapperTest() {
    Member member = Member.builder()
      .email("pandabear")
      .nickname("horaaa")
      .password("1234")
      .address(new Address("seoul", "daegu", "busan"))
      .build();

    SignupRequest signUpRequest = signUpMemberRequestMapper.toDto(member);

    assertEquals(signUpRequest.getNickname(), member.getNickname());
    assertEquals(signUpRequest.getEmail(), member.getEmail());
    assertEquals(signUpRequest.getPassword(), member.getPassword());
    assertEquals(signUpRequest.getAddress(), member.getAddress());
  }
}