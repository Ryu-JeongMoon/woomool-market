package com.woomoolmarket.domain.member.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.woomoolmarket.common.enumeration.Status;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Log4j2
class MemberTest {

    @Test
    @DisplayName("기본 상태 ACTIVE 초기화")
    public void statusTest() {
        Member member = Member.builder()
            .build();

        assertThat(member.getStatus()).isEqualTo(Status.ACTIVE);
    }

    @Test
    @DisplayName("기본 상태 ROLE_USER 초기화")
    public void basicAuthorityTest() {
        Member member = Member.builder()
            .build();

        assertThat(member.getAuthority()).isEqualTo(Authority.ROLE_USER);
    }

    @Test
    @DisplayName("getAuthorityKey() 정상 작동 !~")
    void authorityTest() {
        Member member = Member.builder()
            .authority(Authority.ROLE_ADMIN)
            .build();

        assertThat(member.getAuthorityKey()).isEqualTo(Authority.ROLE_ADMIN.toString());
    }

    @Test
    @DisplayName("leave() 정상 작동 ~!")
    void leaveTest() {
        Member member = Member.builder()
            .nickname("panda")
            .build();

        member.leave();

        assertThat(member.getStatus()).isEqualTo(Status.INACTIVE);
        assertThat(member.getLeaveDateTime()).isNotNull();
    }

    @Test
    @DisplayName("restore() 정상 작동 ~!")
    void restoreTest() {
        Member member = Member.builder()
            .nickname("panda")
            .build();

        member.leave();
        member.restore();

        assertThat(member.getStatus()).isEqualTo(Status.ACTIVE);
        assertThat(member.getLeaveDateTime()).isNull();
    }

    @Test
    @DisplayName("SOCIAL 로그인 사용자 정보 수정")
    void editNicknameAndProfileTest() {
        Member member = Member.builder()
            .nickname("panda")
            .profileImage("bear")
            .build();

        Member result = member.editNicknameAndProfileImage("white", "tiger");

        assertThat(member.getNickname()).isEqualTo(result.getNickname());
        assertThat(member.getProfileImage()).isEqualTo(result.getProfileImage());
    }

    @Test
    @DisplayName("passwordEncoder 변환 성공")
    void passwordTest() {
        PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();

        String rawPassword = "1592";

        Member panda = Member.builder()
            .email("panda@naver.com")
            .password(passwordEncoder.encode(rawPassword))
            .build();

        assertThat(rawPassword).isNotEqualTo(panda.getPassword());
        assertTrue(passwordEncoder.matches(rawPassword, panda.getPassword()));
    }

}