package com.woomoolmarket.service.member;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.config.ServiceTestConfig;
import com.woomoolmarket.domain.member.entity.Authority;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberSearchCondition;
import com.woomoolmarket.service.member.dto.request.ModifyRequest;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import java.util.List;
import java.util.Objects;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.ResourceLocks;

@Log4j2
@Execution(ExecutionMode.CONCURRENT)
class MemberServiceTest extends ServiceTestConfig {

    private static Long SELLER_ID;
    private static String MEMBER_EMAIL;
    private static String NICKNAME = "nick";
    private static String PASSWORD = "pass";

    @BeforeEach
    void init() {
        Member member = memberTestHelper.createUser();
        Member seller = memberTestHelper.createSeller();

        MEMBER_EMAIL = member.getEmail();
        MEMBER_ID = member.getId();
        SELLER_ID = seller.getId();
    }

    @AfterEach
    void clear() {
        memberRepository.deleteAll();
        Objects.requireNonNull(stringRedisTemplate.keys("*")).forEach(k -> stringRedisTemplate.delete(k));
    }

    @Test
    @DisplayName("회원 가입 시 USER 권한")
    void joinTest() {
        MemberResponse memberResponse = memberService.findMemberById(MEMBER_ID);
        assertThat(MEMBER_EMAIL).isEqualTo(memberResponse.getEmail());
        assertThat(memberResponse.getAuthority()).isEqualTo(Authority.ROLE_USER);
    }

    @Test
    @DisplayName("판매자 가입 시 SELLER 권한")
    void joinSellerTest() {
        MemberResponse memberResponse = memberService.findMemberById(SELLER_ID);
        assertThat(memberResponse.getAuthority()).isEqualTo(Authority.ROLE_SELLER);
    }

    @Test
    @DisplayName("회원 수정")
    void editTest() {
        ModifyRequest modifyRequest = ModifyRequest.builder()
            .phone("01012345678")
            .build();

        memberService.editMemberInfo(MEMBER_ID, modifyRequest);
        Member member = memberRepository.findById(MEMBER_ID).get();

        assertThat(member.getPhone()).isEqualTo(modifyRequest.getPhone());
    }

    @Test
    @DisplayName("회원 탈퇴")
    void leaveTest() {
        memberService.leaveSoftly(MEMBER_ID);
        Member member = memberRepository.findById(MEMBER_ID).get();
        assertThat(member.getStatus()).isEqualTo(Status.INACTIVE);
    }

    @Test
    @DisplayName("회원 복구")
    void restoreTest() {
        memberService.leaveSoftly(MEMBER_ID);
        memberService.restore(MEMBER_ID);
        Member member = memberRepository.findById(MEMBER_ID).get();
        assertThat(member.getStatus()).isEqualTo(Status.ACTIVE);
    }

    @Test
    @DisplayName("이전, 다음 아이디 찾아온다")
    void findIdTest() {
        for (int i = 0; i < 3; i++) {
            Member member = Member.builder()
                .email(MEMBER_EMAIL + i)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();
            memberRepository.save(member);
        }

        Member member2 = memberRepository.findByEmail(MEMBER_EMAIL + 1).get();
        Long originId = member2.getId();
        Long nextId = memberService.findNextId(originId);
        Long previousId = memberService.findPreviousId(originId);

        assertThat(nextId).isGreaterThan(originId);
        assertThat(previousId).isLessThan(originId);
    }

    @Test
    @DisplayName("다음 회원 번호 찾기")
    void findNextIdTest() {
        for (int i = 0; i < 2; i++) {
            Member member = Member.builder()
                .email(MEMBER_EMAIL + i)
                .nickname(NICKNAME)
                .password(PASSWORD)
                .build();
            memberRepository.save(member);
        }

        Member member = memberRepository.findByEmail(MEMBER_EMAIL + 0).get();
        Long originId = member.getId();
        Long nextId = memberService.findNextId(originId);
        assertThat(nextId).isGreaterThan(originId);
    }

    @Test
    @DisplayName("어드민 - 전체 조회")
    @ResourceLocks(value = {@ResourceLock(value = "member"), @ResourceLock(value = "seller")})
    void getListByAllTest() {
        MemberSearchCondition condition = MemberSearchCondition
            .builder()
            .build();
        List<MemberResponse> memberResponses = memberService.getListBySearchConditionForAdmin(condition);
        assertThat(memberResponses.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("어드민 - 권한으로 검색")
    @ResourceLocks(value = {@ResourceLock(value = "member"), @ResourceLock(value = "seller")})
    void getListByAuthorityTest() {
        MemberSearchCondition condition = MemberSearchCondition
            .builder()
            .authority(Authority.ROLE_USER)
            .build();
        List<MemberResponse> memberResponses = memberService.getListBySearchConditionForAdmin(condition);
        assertThat(memberResponses.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("어드민 - 이메일로 검색")
    @ResourceLocks(value = {@ResourceLock(value = "member"), @ResourceLock(value = "seller")})
    void getListByEmailTest() {
        MemberSearchCondition condition = MemberSearchCondition
            .builder()
            .email("nav")
            .build();
        List<MemberResponse> memberResponses = memberService.getListBySearchConditionForAdmin(condition);
        assertThat(memberResponses.size()).isEqualTo(1);
    }
}