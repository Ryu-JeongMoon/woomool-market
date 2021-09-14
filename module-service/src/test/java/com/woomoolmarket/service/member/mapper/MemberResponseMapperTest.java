package com.woomoolmarket.service.member.mapper;

import com.woomoolmarket.ModuleCoreApplication;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import java.time.LocalDateTime;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Log4j2
@SpringBootTest(classes = ModuleCoreApplication.class)
class MemberResponseMapperTest {

    @Autowired
    MemberResponseMapper memberResponseMapper;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("createdDate 받아온다")
    void memberMapperTest() {
        Member member = Member.builder()
            .email("panda@gmail.com")
            .userId("pandabear")
            .nickname("panda")
            .password("123456")
            .build();

        Member savedMember = memberRepository.save(member);
        LocalDateTime createdDate = savedMember.getCreatedDate();
        MemberResponse memberResponse = memberResponseMapper.toDto(savedMember);
        Assertions.assertThat(createdDate).isEqualTo(memberResponse.getCreatedDate());
    }
}