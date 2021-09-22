package com.woomoolmarket.domain.member.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.config.TestConfig;
import com.woomoolmarket.domain.board.entity.Board;
import com.woomoolmarket.domain.board.repository.BoardRepository;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import javax.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Log4j2
@DataJpaTest
@Import(TestConfig.class)
class MemberTest {

    @Autowired
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BoardRepository boardRepository;

    /* 테스트 간 auto-increment 공유하지 않도록 매 테스타마다 1로 초기화 해줌
     *  Board 초기화 안 해줘서 에러났다 우하하 팡파레 */
    @BeforeEach
    void initialize() {
        memberRepository.deleteAll();
        em.createNativeQuery("ALTER TABLE MEMBER ALTER COLUMN `member_id` RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE BOARD ALTER COLUMN `board_id` RESTART WITH 1").executeUpdate();
    }

    @Test
    @DisplayName("sql-dialect auto-increment 는 테이블 간 sequence 를 공유하지 않는다")
    void sequenceTest() throws Exception {
        Member member = Member.builder()
            .nickname("panda")
            .build();

        Board board = Board.builder()
            .title("hello-world")
            .build();

        Member savedMember = memberRepository.save(member);
        Board savedBoard = boardRepository.save(board);

        assertThat(savedMember.getId()).isEqualTo(1);
        assertThat(savedBoard.getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("기본 상태 ACTIVE")
    public void statusTest() {
        Member member =
            Member.builder()
                .email("rjrj")
                .nickname("nick")
                .password("1234")
                .build();

        Member findResult = memberRepository.save(member);

        assertThat(findResult.getMemberStatus()).isEqualTo(Status.ACTIVE);
    }

    @Test
    @DisplayName("getAuthorityKey() 정상 작동 !~")
    void authorityTest() {
        Member member = Member.builder()
            .nickname("panda")
            .authority(Authority.ROLE_ADMIN)
            .build();

        String authorityKey = member.getAuthorityKey();
        assertEquals(authorityKey, Authority.ROLE_ADMIN.toString());
    }

    @Test
    @DisplayName("leave() 정상 작동 ~!")
    void leaveTest() {
        Member member = Member.builder()
            .nickname("panda")
            .build();

        member.leave();

        assertEquals(member.getMemberStatus(), Status.INACTIVE);
        assertNotNull(member.getLeaveDateTime());
    }

    @Test
    @DisplayName("restore() 정상 작동 ~!")
    void restoreTest() {
        Member member = Member.builder()
            .nickname("panda")
            .build();

        member.leave();
        member.restore();

        assertEquals(member.getMemberStatus(), Status.ACTIVE);
        assertNull(member.getLeaveDateTime());
    }

    @Test
    @DisplayName("SOCIAL 로그인 사용자 정보 수정")
    void editNicknameAndProfileTest() {
        Member member = memberRepository.save(Member.builder()
            .nickname("panda")
            .password("1234")
            .profileImage("bear")
            .provider(AuthProvider.FACEBOOK)
            .build());

        Member result = member.editNicknameAndProfileImage("white", "tiger");

        assertEquals(member.getNickname(), result.getNickname());
        assertEquals(member.getProfileImage(), result.getProfileImage());
    }

    @Test
    @DisplayName("passwordEncoder 가져온다")
    void passwordTest() {
        PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();

        Member panda = Member.builder()
            .email("panda@naver.com")
            .password(passwordEncoder.encode("1592"))
            .build();

        Member savedMember = memberRepository.save(panda);
        assertNotEquals("1592", panda.getPassword());
        assertTrue(passwordEncoder.matches("1592", savedMember.getPassword()));
    }

}