package com.woomoolmarket.domain.member.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.woomoolmarket.config.TestConfig;
import com.woomoolmarket.domain.member.entity.Member;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
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

        assertEquals(savedMember, member);
        assertEquals(savedMember.getEmail(), member.getEmail());
    }

    @Test
    @DisplayName("삭제된 회원 찾으려 하면 에러난다")
    void deleteTest() {
        Member member = Member.builder()
            .email("rjrj")
            .nickname("dldld")
            .password("1234")
            .build();

        Member savedMember = memberRepository.save(member);
        assertEquals(savedMember, member);
        memberRepository.delete(savedMember);
        assertThrows(RuntimeException.class, () -> memberRepository.findByEmail(member.getEmail())
            .orElseThrow(() -> new RuntimeException("x")));
    }

    @Test
    @DisplayName("previous-id 이전 번호 찾기")
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

        assertEquals(previousId, member1.getId());
    }
}