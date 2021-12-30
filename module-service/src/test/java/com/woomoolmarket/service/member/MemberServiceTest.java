package com.woomoolmarket.service.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.woomoolmarket.common.embeddable.Address;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.config.ServiceTestConfig;
import com.woomoolmarket.domain.member.entity.Authority;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.query.MemberQueryResponse;
import com.woomoolmarket.domain.member.repository.MemberSearchCondition;
import com.woomoolmarket.service.member.dto.request.ModifyRequest;
import com.woomoolmarket.service.member.dto.request.SignupRequest;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import com.woomoolmarket.service.member.mapper.ModifyRequestMapper;
import java.nio.charset.StandardCharsets;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Execution(ExecutionMode.CONCURRENT)
class MemberServiceTest extends ServiceTestConfig {

    private static Long SELLER_ID;
    private static String MEMBER_EMAIL;
    private static String NICKNAME = "nick";
    private static String PASSWORD = "pass";

    @Autowired
    ModifyRequestMapper modifyRequestMapper;

    @BeforeEach
    void init() {
        Member member = memberTestHelper.createMember();
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
    void joinMember() {
        MemberResponse memberResponse = memberService.findMemberById(MEMBER_ID);
        assertThat(MEMBER_EMAIL).isEqualTo(memberResponse.getEmail());
        assertThat(memberResponse.getAuthority()).isEqualTo(Authority.ROLE_USER);
    }

    @Test
    @DisplayName("판매자 가입 시 SELLER 권한")
    void joinSeller() {
        MemberResponse memberResponse = memberService.findMemberById(SELLER_ID);
        assertThat(memberResponse.getAuthority()).isEqualTo(Authority.ROLE_SELLER);
    }

    @Test
    @DisplayName("회원 가입")
    void joinWithMultipartFile() {
        String writerData = "panda panda panda";
        MultipartFile multipartFile = new MockMultipartFile(
            "files",
            "hehe.jpg",
            "image/jpeg",
            writerData.getBytes(StandardCharsets.UTF_8));

        SignupRequest signupRequest = SignupRequest.builder()
            .email("PANDA@naver.com")
            .nickname("nick")
            .password("1234")
            .multipartFile(multipartFile)
            .build();
        Member member = memberService.join(signupRequest, Authority.ROLE_USER);
        assertThat(member.getImage()).isNotNull();
    }

    @Test
    @DisplayName("회원 가입 시 이메일 중복 - IllegalArgumentException 발생")
    void duplicateEmail() {
        SignupRequest signupRequest = SignupRequest.builder()
            .email(MEMBER_EMAIL)
            .nickname("nick")
            .password("1234")
            .build();

        assertThrows(IllegalArgumentException.class, () -> memberService.joinAsMember(signupRequest));
    }

    @Test
    @DisplayName("회원 수정")
    void edit() {
        ModifyRequest modifyRequest = ModifyRequest.builder()
            .phone("01012345678")
            .build();

        memberService.edit(MEMBER_ID, modifyRequest);
        Member member = memberRepository.findById(MEMBER_ID).get();

        assertThat(member.getPhone()).isEqualTo(modifyRequest.getPhone());
    }

    @Test
    @DisplayName("회원 이미지 수정")
    void editImage() {
        String writerData = "panda panda panda";
        MultipartFile multipartFile = new MockMultipartFile(
            "files",
            "hehe.jpg",
            "image/jpeg",
            writerData.getBytes(StandardCharsets.UTF_8));

        ModifyRequest modifyRequest = ModifyRequest.builder()
            .phone("01012345678")
            .multipartFile(multipartFile)
            .build();

        memberService.edit(MEMBER_ID, modifyRequest);
        Member member = memberRepository.findById(MEMBER_ID).get();

        assertThat(member.getImage()).isNotNull();
    }

    @Test
    @DisplayName("회원 전체 수정")
    void editTotal() {
        String nickname = "nickname";
        Address address = new Address("123456", "123456", "123456");
        String license = "1234567890";
        String phone = "01234567891";
        String profileImage = "profileImage";

        ModifyRequest modifyRequest = ModifyRequest.builder()
            .nickname(nickname)
            .address(address)
            .license(license)
            .phone(phone)
            .profileImage(profileImage)
            .build();

        Member member = memberRepository.findById(MEMBER_ID).get();
        modifyRequestMapper.updateFromDto(modifyRequest, member);

        assertThat(member.getNickname()).isEqualTo(modifyRequest.getNickname());
        assertThat(member.getAddress()).isEqualTo(modifyRequest.getAddress());
        assertThat(member.getLicense()).isEqualTo(modifyRequest.getLicense());
        assertThat(member.getPhone()).isEqualTo(modifyRequest.getPhone());
        assertThat(member.getProfileImage()).isEqualTo(modifyRequest.getProfileImage());
    }

    @Test
    @DisplayName("회원 탈퇴")
    void leave() {
        memberService.leaveSoftly(MEMBER_ID);
        Member member = memberRepository.findById(MEMBER_ID).get();
        assertThat(member.getStatus()).isEqualTo(Status.INACTIVE);
    }

    @Test
    @DisplayName("회원 복구")
    void restore() {
        memberService.leaveSoftly(MEMBER_ID);
        memberService.restore(MEMBER_ID);
        Member member = memberRepository.findById(MEMBER_ID).get();
        assertThat(member.getStatus()).isEqualTo(Status.ACTIVE);
    }

    @Test
    @DisplayName("이전, 다음 아이디 찾아온다")
    void findId() {
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
    void findNextId() {
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
    void getListByAll() {
        MemberSearchCondition condition = MemberSearchCondition
            .builder()
            .build();
        Page<MemberQueryResponse> queryResponsePage = memberService.searchForAdminBy(condition, Pageable.ofSize(10));
        assertThat(queryResponsePage.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("어드민 - 권한으로 검색")
    @ResourceLocks(value = {@ResourceLock(value = "member"), @ResourceLock(value = "seller")})
    void getListByAuthority() {
        MemberSearchCondition condition = MemberSearchCondition
            .builder()
            .authority(Authority.ROLE_USER)
            .build();
        Page<MemberQueryResponse> queryResponsePage = memberService.searchForAdminBy(condition, Pageable.ofSize(10));
        assertThat(queryResponsePage.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("어드민 - 이메일로 검색")
    @ResourceLocks(value = {@ResourceLock(value = "member"), @ResourceLock(value = "seller")})
    void getListByEmail() {
        MemberSearchCondition condition = MemberSearchCondition
            .builder()
            .email("nav")
            .build();
        Page<MemberQueryResponse> queryResponsePage = memberService.searchForAdminBy(condition, Pageable.ofSize(10));
        assertThat(queryResponsePage.getTotalElements()).isEqualTo(1);
    }
}