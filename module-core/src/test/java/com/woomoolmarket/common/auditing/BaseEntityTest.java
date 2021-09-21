package com.woomoolmarket.common.auditing;

import static org.junit.jupiter.api.Assertions.*;

import com.woomoolmarket.config.TestConfig;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Log4j2
@Import(TestConfig.class)
@DataJpaTest
class BaseEntityTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("EntityListeners 속성 상속된다")
    void baseEntityTest() {
        Member member = Member.builder()
            .email("panda")
            .build();

        Member result = memberRepository.save(member);

        log.info("result = {}", result.getCreatedDate());
        assertNotEquals(null, result.getCreatedDate());
    }
}