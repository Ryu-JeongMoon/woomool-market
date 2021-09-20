package com.woomoolmarket.domain.member.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.config.TestConfig;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Log4j2
@DataJpaTest
@Import(TestConfig.class)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void joinTest() {
        Member member = Member.builder()
            .email("rjrj")
            .nickname("dldld")
            .password("1234")
            .build();

        Member savedMember = memberRepository.save(member);

        assertThat(savedMember).isEqualTo(member);
        assertThat(savedMember.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    void deleteTest() {
        Member member = Member.builder()
            .email("rjrj")
            .nickname("dldld")
            .password("1234")
            .build();

        Member savedMember = memberRepository.save(member);

        log.info("member.email = {}", savedMember.getEmail());
        log.info("member.nickname = {}", savedMember.getNickname());
        log.info("member.password = {}", savedMember.getPassword());
        log.info("member.leaveDate = {}", savedMember.getLeaveDateTime());

        assertThat(savedMember).isEqualTo(member);

        memberRepository.delete(savedMember);

        assertThrows(RuntimeException.class, () -> memberRepository.findByEmail(member.getEmail())
            .orElseThrow(() -> new RuntimeException("x")));
    }

    @Test
    void findPreviousIdTest() {
        for (int i = 0; i < 3; i++) {
            Member member = Member.builder()
                    .email("rjrj" + i)
                    .build();
            memberRepository.save(member);
        }

        Member member1 = memberRepository.findByEmail("rjrj1").get();
        Member member2 = memberRepository.findByEmail("rjrj2").get();
        Long previousId = memberRepository.findPreviousId(member2.getId())
            .orElseThrow(() -> new RuntimeException("전 회원 없음?!"));

        assertThat(previousId).isEqualTo(member1.getId());
    }
}