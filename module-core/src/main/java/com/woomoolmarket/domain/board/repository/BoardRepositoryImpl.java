package com.woomoolmarket.domain.board.repository;

import static com.woomoolmarket.domain.board.entity.QBoard.board;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.common.util.QueryDslUtils;
import com.woomoolmarket.domain.board.entity.BoardCategory;
import com.woomoolmarket.domain.board.query.BoardQueryResponse;
import com.woomoolmarket.domain.board.query.QBoardQueryResponse;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private Page<BoardQueryResponse> findByTemplate(Pageable pageable, BooleanBuilder booleanBuilder) {
        QueryResults<BoardQueryResponse> results = queryFactory
            .select(new QBoardQueryResponse(
                board.id, board.title, board.content, board.hit, board.boardCategory, board.member.email,
                board.endDateTime, board.startDateTime, board.createdDateTime))
            .from(board)
            .leftJoin(board.member)
            .where(booleanBuilder)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(board.id.desc())
            .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public Page<BoardQueryResponse> searchBy(BoardSearchCondition condition, Pageable pageable) {
        return findByTemplate(pageable, combineBy(condition));
    }

    @Override
    public Page<BoardQueryResponse> searchByAdmin(BoardSearchCondition condition, Pageable pageable) {
        return findByTemplate(pageable, combineForAdminBy(condition));
    }

    private BooleanBuilder combineBy(BoardSearchCondition condition) {
        return emailContains(condition.getEmail())
            .and(titleContains(condition.getTitle()))
            .and(contentContains(condition.getContent()))
            .and(statusEq(Status.ACTIVE))
            .and(categoryEq(condition.getBoardCategory()))
            .and(board.startDateTime.before(LocalDateTime.now()))
            .and(board.endDateTime.after(LocalDateTime.now()));
    }

    private BooleanBuilder combineForAdminBy(BoardSearchCondition condition) {
        return emailContains(condition.getEmail())
            .and(titleContains(condition.getTitle()))
            .and(contentContains(condition.getContent()))
            .and(statusEq(condition.getStatus()))
            .and(categoryEq(condition.getBoardCategory()));
    }

    private BooleanBuilder emailContains(String email) {
        return QueryDslUtils.nullSafeBuilder(() -> board.member.email.contains(email));
    }

    private BooleanBuilder titleContains(String title) {
        return QueryDslUtils.nullSafeBuilder(() -> board.title.contains(title));
    }

    private BooleanBuilder contentContains(String content) {
        return QueryDslUtils.nullSafeBuilder(() -> board.content.contains(content));
    }

    private BooleanBuilder statusEq(Status status) {
        return QueryDslUtils.nullSafeBuilder(() -> board.status.eq(status));
    }

    private BooleanBuilder categoryEq(BoardCategory category) {
        return QueryDslUtils.nullSafeBuilder(() -> board.boardCategory.eq(category));
    }
}