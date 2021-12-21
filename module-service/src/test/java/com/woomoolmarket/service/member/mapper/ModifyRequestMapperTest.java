package com.woomoolmarket.service.member.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.woomoolmarket.domain.member.entity.Authority;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.service.member.MemberService;
import com.woomoolmarket.service.member.dto.request.ModifyRequest;
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
class ModifyRequestMapperTest {

    private static final String EMAIL = "pandabear";

    @Autowired
    EntityManager em;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ModifyRequestMapper modifyRequestMapper;

    @BeforeEach
    void init() {
        Member member = Member.builder()
            .email(EMAIL)
            .nickname("nick")
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
        ModifyRequest modifyRequest = createModifyDto();
        Member saveResult = memberRepository.findByEmail(EMAIL).get();

        modifyRequestMapper.updateFromDto(modifyRequest, saveResult);

        Member findResult = memberRepository.findByEmail(EMAIL)
            .orElseGet(() -> Member.builder().password("0000").profileImage("0000").license("0000").build());

        assertEquals(findResult.getPassword(), modifyRequest.getPassword());
        assertEquals(findResult.getProfileImage(), modifyRequest.getProfileImage());
        assertEquals(findResult.getPhone(), modifyRequest.getPhone());
        assertEquals(findResult.getLicense(), modifyRequest.getLicense());
    }

    @Test
    @DisplayName("editInfo() 동작한다")
    void updateMemberTest() {
        ModifyRequest modifyRequest = createModifyDto();
        Member member = memberRepository.findByEmail(EMAIL).get();
        memberService.edit(member.getId(), modifyRequest);

        Member findResult = memberRepository.findByEmail(EMAIL).get();

        assertEquals(findResult.getPassword(), modifyRequest.getPassword());
        assertEquals(findResult.getProfileImage(), modifyRequest.getProfileImage());
        assertEquals(findResult.getPhone(), modifyRequest.getPhone());
        assertEquals(findResult.getLicense(), modifyRequest.getLicense());
    }

    private ModifyRequest createModifyDto() {
        return ModifyRequest.builder()
            .password("5678")
            .profileImage("bear")
            .phone("5678")
            .license("5678")
            .build();
    }
}