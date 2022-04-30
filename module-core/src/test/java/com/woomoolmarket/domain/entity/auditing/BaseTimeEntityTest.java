package com.woomoolmarket.domain.entity.auditing;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.config.TestConfig;
import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(TestConfig.class)
@DataJpaTest
class BaseTimeEntityTest {

  @Autowired
  MemberRepository memberRepository;

  @Test
  @DisplayName("JPA auditing 동작한다")
  void baseTimeTest() {
    Member member = Member.builder()
      .email("panda-bear")
      .nickname("nick")
      .password("pass")
      .build();

    Member result = memberRepository.save(member);
    assertThat(result.getCreatedDateTime()).isNotNull();
    assertThat(result.getLastModifiedDateTime()).isNotNull();
  }
}