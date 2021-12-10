package com.woomoolmarket.domain.board.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woomoolmarket.config.TestConfig;
import com.woomoolmarket.domain.board.entity.Board;
import com.woomoolmarket.domain.board.entity.BoardCategory;
import com.woomoolmarket.domain.board.entity.QBoard;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Log4j2
@DataJpaTest
@Import(TestConfig.class)
class BoardRepositoryImplTest {

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    JPAQueryFactory queryFactory;
    @Autowired
    EntityManager em;

    @BeforeEach
    void init() {
        boardRepository.deleteAll();
        em.createNativeQuery("ALTER TABLE BOARD ALTER COLUMN `board_id` RESTART WITH 1").executeUpdate();

        Board board = Board.builder()
            .title("panda1")
            .content("bear1")
            .boardCategory(BoardCategory.QNA)
            .startDateTime(LocalDateTime.now())
            .endDateTime(LocalDateTime.of(2099, 1, 1, 1, 1, 1))
            .build();

        boardRepository.save(board);
    }

    @Test
    void containsTest() {
        Board board = queryFactory.selectFrom(QBoard.board)
            .where(QBoard.board.title.contains("pa"))
            .fetchOne();

        assertThat(board).isNotNull();
    }
}