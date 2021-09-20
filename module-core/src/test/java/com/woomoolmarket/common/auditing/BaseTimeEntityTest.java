package com.woomoolmarket.common.auditing;

import static org.junit.jupiter.api.Assertions.*;

import com.woomoolmarket.config.TestConfig;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Log4j2
@Import(TestConfig.class)
@DataJpaTest
class BaseTimeEntityTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("JPA auditing 동작한다")
    void baseTimeTest() {
        Member member = Member.builder()
            .email("panda")
            .build();

        Member result = memberRepository.save(member);
        assertNotNull(result.getCreatedDate());
        assertNotNull(result.getLastModifiedDate());
    }
}