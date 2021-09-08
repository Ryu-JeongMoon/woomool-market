package com.woomoolmarket.service.member.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.ModuleServiceApplication;
import com.woomoolmarket.entity.member.entity.Address;
import com.woomoolmarket.entity.member.entity.Member;
import com.woomoolmarket.entity.member.entity.MemberStatus;
import com.woomoolmarket.entity.member.repository.MemberRepository;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import com.woomoolmarket.service.member.mapper.MemberResponseMapper;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Transactional
@SpringBootTest(classes = ModuleServiceApplication.class)
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberResponseMapper memberResponseMapper;

    @Test
    void dateCompareTest() {
        LocalDateTime septemberFirst = LocalDateTime.of(2021, 9, 1, 8, 15, 50);
        System.out.println(LocalDateTime.now()
            .compareTo(septemberFirst));
    }

    @Test
    void findIdTest() {
        for (int i = 8; i < 12; i++) {
            Member member = Member.builder()
                .email("panda@naver.com" + i)
                .nickname("nick" + i)
                .userId("ponda1" + i)
                .password("123456")
                .address(new Address("seoul", "yeonhui", "1234"))
                .memberStatus(MemberStatus.ACTIVE)
                .build();

            memberRepository.save(member);
        }

        Member member1 = memberRepository.findByEmail("panda@naver.com9").get();
        Member member2 = memberRepository.findByEmail("panda@naver.com10").get();
        Member member3 = memberRepository.findByEmail("panda@naver.com11").get();

        Long nextId = memberService.findNextId(memberResponseMapper.toDto(member2))
            .getId();
        Long previousId = memberService.findPreviousId(memberResponseMapper.toDto(member2))
            .getId();

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

        List<MemberResponse> allMembers = memberService.findAllMembers();

        for (MemberResponse allMember : allMembers) {
            System.out.println("allMember.getId() = " + allMember.getId());
        }
        Long nextId = memberService.findNextId(5L).get();
        MemberResponse memberResponse = memberService.findMember(5L);
        MemberResponse nextMember = memberService.findNextId(memberResponse);
        assertThat(nextId).isEqualTo(nextMember.getId());
    }
}