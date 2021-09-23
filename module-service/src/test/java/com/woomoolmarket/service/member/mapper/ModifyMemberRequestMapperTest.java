package com.woomoolmarket.service.member.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.woomoolmarket.domain.member.entity.Authority;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.service.member.MemberService;
import com.woomoolmarket.service.member.dto.request.ModifyMemberRequest;
import javax.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Transactional
@SpringBootTest
class ModifyMemberRequestMapperTest {

    @Autowired
    MemberService memberService;
    @Autowired
    ModifyMemberRequestMapper modifyMemberRequestMapper;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @BeforeEach
    void init() {
        Member member = Member.builder()
            .email("panda")
            .password("1234")
            .profileImage("panda")
            .phone("1234")
            .license("1234")
            .authority(Authority.ROLE_ADMIN)
            .build();

        memberRepository.save(member);
    }

    @Test
    @DisplayName("updateFromDto 동작한다")
    void updateTest() {
        ModifyMemberRequest modifyMemberRequest = createModifyDto();
        Member saveResult = memberRepository.findByEmail("panda").get();

        modifyMemberRequestMapper.updateFromDto(modifyMemberRequest, saveResult);

        Member findResult = memberRepository.findByEmail("panda")
            .orElseGet(() -> Member.builder().password("0000").profileImage("0000").license("0000").build());

        assertEquals(findResult.getPassword(), modifyMemberRequest.getPassword());
        assertEquals(findResult.getProfileImage(), modifyMemberRequest.getProfileImage());
        assertEquals(findResult.getPhone(), modifyMemberRequest.getPhone());
        assertEquals(findResult.getLicense(), modifyMemberRequest.getLicense());
    }

    @Test
    @DisplayName("editInfo() 동작한다")
    void updateMemberTest() {
        ModifyMemberRequest modifyMemberRequest = createModifyDto();
        Member member = memberRepository.findByEmail("panda").get();
        memberService.editInfo(member.getId(), modifyMemberRequest);

        Member findResult = memberRepository.findByEmail("panda").get();

        assertEquals(findResult.getPassword(), modifyMemberRequest.getPassword());
        assertEquals(findResult.getProfileImage(), modifyMemberRequest.getProfileImage());
        assertEquals(findResult.getPhone(), modifyMemberRequest.getPhone());
        assertEquals(findResult.getLicense(), modifyMemberRequest.getLicense());
    }

    private ModifyMemberRequest createModifyDto() {
        return ModifyMemberRequest.builder()
            .password("5678")
            .profileImage("bear")
            .phone("5678")
            .license("5678")
            .build();
    }
}