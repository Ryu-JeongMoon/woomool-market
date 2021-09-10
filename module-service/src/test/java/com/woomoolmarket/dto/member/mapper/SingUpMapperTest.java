package com.woomoolmarket.dto.member.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.ModuleServiceApplication;
import com.woomoolmarket.model.member.entity.Address;
import com.woomoolmarket.model.member.entity.Member;
import com.woomoolmarket.model.member.entity.MemberStatus;
import com.woomoolmarket.model.member.entity.Authority;
import com.woomoolmarket.model.member.entity.Social;
import com.woomoolmarket.service.member.dto.request.SignUpMemberRequest;
import com.woomoolmarket.service.member.mapper.SignUpMemberRequestMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Log4j2
@SpringBootTest(classes = ModuleServiceApplication.class)
class SingUpMapperTest {

    @Autowired
    SignUpMemberRequestMapper signUpRequestMapper;

    @Test
    void mapperTest() {
        List<Authority> authorities = new ArrayList<>();
        authorities.add(Authority.ROLE_USER);
        Member member = new Member("1234", "yaho@", "nick", "1234", "profile", "0101234", "1324",
            new Address("seoul", "str", "zip"), authorities, Social.LOCAL, MemberStatus.ACTIVE, null);

        SignUpMemberRequest signUpRequest = signUpRequestMapper.toDto(member);

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