package com.woomoolmarket.dto.member.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.domain.member.entity.Address;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.service.member.dto.request.SignUpMemberRequest;
import com.woomoolmarket.service.member.mapper.SignUpMemberRequestMapperImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

@Log4j2
class SingUpMapperTest {

    private final SignUpMemberRequestMapperImpl signUpMemberRequestMapper = new SignUpMemberRequestMapperImpl();

    @Test
    void mapperTest() {

        Member member = Member.builder()
            .email("panda")
            .nickname("horaaa")
            .password("1234")
            .address(new Address("seoul", "daegu", "busan"))
            .build();

        SignUpMemberRequest signUpRequest = signUpMemberRequestMapper.toDto(member);

        assertThat(signUpRequest.getNickname()).isEqualTo(member.getNickname());
        assertThat(signUpRequest.getEmail()).isEqualTo(member.getEmail());
        assertThat(signUpRequest.getPassword()).isEqualTo(member.getPassword());
        assertThat(signUpRequest.getAddress()).isEqualTo(member.getAddress());

        log.info("dto.nickname = {}", signUpRequest.getNickname());
        log.info("dto.email = {}", signUpRequest.getEmail());
        log.info("dto.password = {}", signUpRequest.getPassword());
        log.info("dto.address = {}", signUpRequest.getAddress());
    }
}