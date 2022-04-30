package com.woomoolmarket.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.woomoolmarket.config.AbstractDataJpaTest;
import com.woomoolmarket.domain.entity.Board;
import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.entity.enumeration.BoardCategory;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
class MemberRepositoryTest extends AbstractDataJpaTest {

  private final String MEMBER_EMAIL = "panda-bear@gmail.com";

  /* 테스트 간 auto-increment 공유하지 않도록 매 테스타마다 1로 초기화 해줌 */
  @BeforeEach
  void init() {
    memberRepository.deleteAll();
    entityManager.createNativeQuery("ALTER TABLE MEMBER ALTER COLUMN `member_id` RESTART WITH 1").executeUpdate();
    entityManager.createNativeQuery("ALTER TABLE BOARD ALTER COLUMN `board_id` RESTART WITH 1").executeUpdate();
  }

  @Test
  @DisplayName("sql-dialect auto-increment 는 테이블 간 sequence 를 공유하지 않는다")
  void sequenceTest() {
    Member member = Member.builder()
      .email(MEMBER_EMAIL)
      .password("123456")
      .nickname("panda")
      .build();

    Board board = Board.builder()
      .title("hello")
      .content("world")
      .boardCategory(BoardCategory.FREE)
      .startDateTime(LocalDateTime.now())
      .endDateTime(LocalDateTime.of(2099, 1, 1, 1, 1, 1))
      .build();

    Member savedMember = memberRepository.save(member);
    Board savedBoard = boardRepository.save(board);

    assertThat(savedMember.getId()).isEqualTo(1);
    assertThat(savedBoard.getId()).isEqualTo(1);
  }

  @Test
  void joinTest() {
    // given
    Member member = Member.builder()
      .email(MEMBER_EMAIL + 1)
      .nickname("dldld")
      .password("1234")
      .build();

    // when
    Member savedMember = memberRepository.save(member);

    // then
    assertEquals(savedMember, member);
    assertEquals(savedMember.getEmail(), member.getEmail());
  }

  @Test
  @DisplayName("삭제된 회원 찾으려 하면 에러난다")
  void deleteTest() {
    Member member = Member.builder()
      .email(MEMBER_EMAIL)
      .password("123456")
      .nickname("panda")
      .build();
    Member savedMember = memberRepository.save(member);
    memberRepository.delete(savedMember);
    assertThrows(RuntimeException.class, () -> memberRepository.findByEmail(savedMember.getEmail())
      .orElseThrow(() -> new RuntimeException("x")));
  }

  @Test
  @DisplayName("previous-id 이전 번호 찾기")
  void findPreviousIdTest() {
    for (int i = 0; i < 3; i++) {
      Member member = Member.builder()
        .email(MEMBER_EMAIL + i)
        .nickname("nick")
        .password("pass")
        .build();
      memberRepository.save(member);
    }

    Member member1 = memberRepository.findByEmail(MEMBER_EMAIL + 1).get();
    Member member2 = memberRepository.findByEmail(MEMBER_EMAIL + 2).get();

    Long previousId = memberRepository.findPreviousId(member2.getId())
      .orElseThrow(() -> new RuntimeException("전 회원 없음?!"));

    assertEquals(previousId, member1.getId());
  }
}