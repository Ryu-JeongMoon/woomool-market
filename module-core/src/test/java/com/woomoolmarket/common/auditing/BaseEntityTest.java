package com.woomoolmarket.common.auditing;

import static org.assertj.core.api.Assertions.assertThat;

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
class BaseEntityTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("EntityListeners 속성 상속된다")
    void baseEntityTest() {
        Member member = Member.builder()
            .email("panda-bear")
            .nickname("nick")
            .password("pass")
            .build();

        Member result = memberRepository.save(member);
        assertThat(result.getCreatedDateTime()).isNotNull();
    }
}