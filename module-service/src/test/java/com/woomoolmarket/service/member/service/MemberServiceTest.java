package com.woomoolmarket.service.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.woomoolmarket.domain.member.entity.Address;
import com.woomoolmarket.domain.member.entity.Authority;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.service.member.MemberService;
import com.woomoolmarket.service.member.dto.request.SignUpRequest;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import com.woomoolmarket.service.member.mapper.MemberResponseMapper;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Transactional
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberResponseMapper memberResponseMapper;
    @Autowired
    EntityManager em;

    @BeforeEach
    void initialize() {
        memberRepository.deleteAll();
        em.createNativeQuery("ALTER TABLE MEMBER ALTER COLUMN `member_id` RESTART WITH 1").executeUpdate();
    }

    @Test
    void dateCompareTest() {
        LocalDateTime septemberFirst = LocalDateTime.of(2021, 9, 1, 8, 15, 50);
        System.out.println(LocalDateTime.now().compareTo(septemberFirst));
    }

    @Test
    void findIdTest() {
        for (int i = 8; i < 12; i++) {
            Member member = Member.builder()
                .email("panda@naver.com" + i)
                .nickname("nick" + i)
                .password("123456")
                .address(new Address("seoul", "yeonhui", "1234"))
                .build();

            memberRepository.save(member);
        }

        Member member1 = memberRepository.findByEmail("panda@naver.com9").get();
        Member member2 = memberRepository.findByEmail("panda@naver.com10").get();
        Member member3 = memberRepository.findByEmail("panda@naver.com11").get();

        Long nextId = memberService.findNextId(memberResponseMapper.toDto(member2).getId());
        Long previousId = memberService.findPreviousId(memberResponseMapper.toDto(member2).getId());

        log.info("member1.id = {}", member1.getId());
        log.info("member2.id = {}", member2.getId());
        log.info("member3.id = {}", member3.getId());

        assertThat(nextId).isEqualTo(member3.getId());
        assertThat(previousId).isEqualTo(member1.getId());
    }

    /* 뭐지 별 차이 안 나네 둘 다 느린 거 같은데 .. Long 으로 직접 구하는게 빠르긴 함 */
    @Test
    void findNextIdTest() {
        for (int i = 0; i < 7; i++) {
            Member member = Member.builder()
                .email("rjrj" + i)
                .build();
            memberRepository.save(member);
        }

        Page<MemberResponse> allMembers = memberService.findAllMembers(Pageable.unpaged());

        for (MemberResponse allMember : allMembers) {
            System.out.println("allMember.getId() = " + allMember.getId());
        }

        Long nextId = memberService.findNextId(5L);
        MemberResponse nextMember = memberService.findMember(nextId);
        assertThat(nextId).isEqualTo(nextMember.getId());
    }

    @Test
    @DisplayName("Authority SELLER 로 들어감")
    void joinSellerTest() {
        SignUpRequest seller = SignUpRequest.builder()
            .email("panda")
            .nickname("bear")
            .password("1234")
            .build();

        Long findId = memberService.joinAsSeller(seller);
        MemberResponse memberResponse = memberService.findMember(findId);

        assertEquals(memberResponse.getAuthority(), Authority.ROLE_SELLER);
    }

    @Test
    @DisplayName("중복 회원 가입")
    void duplicateTest() {
        SignUpRequest member1 = SignUpRequest.builder()
            .email("panda")
            .nickname("bear")
            .password("1234")
            .build();

        SignUpRequest member2 = SignUpRequest.builder()
            .email("panda")
            .nickname("bear")
            .password("1234")
            .build();

        memberService.joinAsMember(member1);
        memberService.joinAsMember(member2);


    }
}