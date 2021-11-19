package com.woomoolmarket.domain.board.repository;

import static com.woomoolmarket.domain.board.entity.QBoard.board;
import static com.woomoolmarket.domain.member.entity.QMember.member;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.common.querydsl.QueryDslUtil;
import com.woomoolmarket.domain.board.dto.response.BoardResponse;
import com.woomoolmarket.domain.board.dto.response.QBoardResponse;
import com.woomoolmarket.domain.board.entity.Board;
import com.woomoolmarket.domain.board.entity.BoardCategory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BoardResponse> findByConditionAndPage(BoardSearchCondition searchCondition, Pageable pageable) {
        QueryResults<BoardResponse> results = queryFactory
            .select(new QBoardResponse(
                board.id, board.title, board.content, board.hit, board.boardCategory, board.member.email,
                board.endDateTime, board.startDateTime, board.createdDateTime))
            .from(board)
            .leftJoin(board.member)
            .where(
                searchBy(searchCondition),
                board.startDateTime.before(LocalDateTime.now()),
                board.endDateTime.after(LocalDateTime.now()))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(board.id.desc())
            .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public List<Board> findByConditionForAdmin(BoardSearchCondition searchCondition) {
        return queryFactory
            .selectFrom(board)
            .leftJoin(board.member, member)
            .fetchJoin()
            .where(searchForAdminBy(searchCondition))
            .fetch();
    }

    @Override
    public Page<BoardResponse> findByStatus(Pageable pageable, Status status) {
        QueryResults<BoardResponse> results =
            queryFactory
                .select(new QBoardResponse(
                    board.id, board.title, board.content, board.hit, board.boardCategory, board.member.email,
                    board.endDateTime, board.startDateTime, board.createdDateTime))
                .from(board)
                .leftJoin(board.member)
                .where(board.status.eq(status),
                    board.startDateTime.before(LocalDateTime.now()),
                    board.endDateTime.after(LocalDateTime.now()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(board.id.desc())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    private BooleanBuilder emailContains(String email) {
        return QueryDslUtil.nullSafeBuilder(() -> board.member.email.contains(email));
    }

    private BooleanBuilder titleContains(String title) {
        return QueryDslUtil.nullSafeBuilder(() -> board.title.contains(title));
    }

    private BooleanBuilder contentContains(String content) {
        return QueryDslUtil.nullSafeBuilder(() -> board.content.contains(content));
    }

    private BooleanBuilder statusEq(Status status) {
        return QueryDslUtil.nullSafeBuilder(() -> board.status.eq(status));
    }

    private BooleanBuilder categoryEq(BoardCategory category) {
        return QueryDslUtil.nullSafeBuilder(() -> board.boardCategory.eq(category));
    }

    // 회원은 활성화된 게시글만 볼 수 있도록 넘어오는 값 사용하지 않음
    private BooleanBuilder searchBy(BoardSearchCondition searchCondition) {
        return emailContains(searchCondition.getEmail())
            .and(titleContains(searchCondition.getTitle()))
            .and(contentContains(searchCondition.getContent()))
            .and(statusEq(Status.ACTIVE))
            .and(categoryEq(searchCondition.getBoardCategory()));
    }

    private BooleanBuilder searchForAdminBy(BoardSearchCondition searchCondition) {
        return emailContains(searchCondition.getEmail())
            .and(titleContains(searchCondition.getTitle()))
            .and(contentContains(searchCondition.getContent()))
            .and(statusEq(searchCondition.getStatus()))
            .and(categoryEq(searchCondition.getBoardCategory()));
    }
}

/*
BooleanBuilder 놈들 분리해둬야 할까?

searchByAll, searchByAllForAdmin 중복
검색 메서드 추가될 경우 중복 제거하기
 */