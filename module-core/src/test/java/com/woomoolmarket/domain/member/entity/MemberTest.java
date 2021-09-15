package com.woomoolmarket.domain.member.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.woomoolmarket.domain.board.entity.Board;
import com.woomoolmarket.domain.board.repository.BoardRepository;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
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
    @Description(value = "sql-dialect auto-increment 는 테이블 간 sequence 를 공유하지 않는다!")
    void sequenceTest() throws Exception {
        //given
        Member member = Member.builder()
            .nickname("panda")
            .build();

        Board board = Board.builder()
            .title("hello-world")
            .build();

        //when
        Member savedMember = memberRepository.save(member);
        Board savedBoard = boardRepository.save(board);

        //then
        assertThat(savedMember.getId()).isEqualTo(1);
        assertThat(savedBoard.getId()).isEqualTo(1);
    }

    @Test
    @Description(value = "changeMember() 메서드를 통해 기존 member 값도 변경 된다!")
    void changeTest() {

        Member member = memberRepository.save(Member.builder()
            .nickname("panda")
            .password("1234")
            .build());

        Member newMember = Member.builder()
            .password("5678")
            .build();

        Member savedMember = member.editMemberInfo(newMember);

        assertThat(savedMember.getPassword()).isEqualTo(member.getPassword());
        assertThat(savedMember.getNickname()).isEqualTo(member.getNickname());
    }

    @Test
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