package com.woomoolmarket.service.member.mapper;

import static org.assertj.core.api.Assertions.*;

import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.domain.member.dto.response.MemberResponse;
import java.time.LocalDateTime;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Log4j2
@SpringBootTest
class MemberResponseMapperTest {

    @Autowired
    MemberRepository memberRepository;

    private MemberResponseMapperImpl memberResponseMapper = new MemberResponseMapperImpl();

    @BeforeEach
    void init() {
        Member member = Member.builder()
            .email("panda@gmail.com")
            .nickname("panda")
            .password("123456")
            .build();

        memberRepository.save(member);
    }

    @Test
    @DisplayName("createdDate 받아온다")
    void memberMapperTest() {
        Member findResult = memberRepository.findByEmail("panda@gmail.com").get();
        LocalDateTime createdDate = findResult.getCreatedDateTime();
        MemberResponse memberResponse = memberResponseMapper.toDto(findResult);
        assertThat(createdDate).isEqualTo(memberResponse.getCreatedDateTime());
    }
}