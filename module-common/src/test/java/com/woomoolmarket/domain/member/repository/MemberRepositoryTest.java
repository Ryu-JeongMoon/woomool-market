package com.woomoolmarket.domain.member.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.woomoolmarket.ModuleCommonApplication;
import com.woomoolmarket.domain.member.entity.Member;
import javax.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Transactional
@SpringBootTest(classes = ModuleCommonApplication.class)
class MemberRepositoryTest {

    @Autowired
    EntityManager em;
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
        log.info("member.leaveDate = {}", savedMember.getLeaveDate());

        assertThat(savedMember).isEqualTo(member);

        memberRepository.delete(savedMember);

        assertThrows(RuntimeException.class, () -> memberRepository.findByEmail(member.getEmail())
            .orElseThrow(() -> new RuntimeException("x")));
    }

    @Test
    void findPreviousIdTest() {
        for (int i = 0; i < 5; i++) {
            Member member = Member.builder()
                .email("rjrj" + i)
                .nickname("dldld" + i)
                .password("1234" + i)
                .build();
            memberRepository.save(member);
        }

        em.flush();
        em.clear();

        Long id = 3L;
        Long previousId = memberRepository.findPreviousId(id)
            .orElseThrow(() -> new RuntimeException("전 회원 없음?!"));

        assertThat(previousId).isEqualTo(2L);
    }
}