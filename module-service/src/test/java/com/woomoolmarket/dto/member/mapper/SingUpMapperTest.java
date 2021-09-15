package com.woomoolmarket.dto.member.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.domain.member.entity.Address;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.entity.Social;
import com.woomoolmarket.service.member.dto.request.SignUpMemberRequest;
import com.woomoolmarket.service.member.mapper.SignUpMemberRequestMapper;
import com.woomoolmarket.service.member.mapper.SignUpMemberRequestMapperImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@Log4j2
class SingUpMapperTest {

    @Autowired
    private SignUpMemberRequestMapperImpl signUpMemberRequestMapper;

    @Test
    void mapperTest() {

        Member member = new Member("1234", "yaho@", "nick", "1234", "profile", "0101234", "1324",
            new Address("seoul", "str", "zip"), Social.LOCAL);

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