package com.woomoolmarket.domain.board.repository;

import static com.woomoolmarket.domain.board.entity.QBoard.board;
import static com.woomoolmarket.domain.member.entity.QMember.*;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.common.querydsl.QueryDslUtil;
import com.woomoolmarket.domain.board.entity.Board;
import com.woomoolmarket.domain.board.entity.BoardCategory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Board> findByCondition(BoardSearchCondition searchCondition) {
        return queryFactory
            .selectFrom(board)
            .leftJoin(board.member, member)
            .fetchJoin()
            .where(searchByAll(searchCondition))
            .fetch();
    }

    @Override
    public List<Board> findByConditionForAdmin(BoardSearchCondition searchCondition) {
        return queryFactory
            .selectFrom(board)
            .leftJoin(board.member, member)
            .fetchJoin()
            .where(searchByAllForAdmin(searchCondition))
            .fetch();
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

    private BooleanBuilder searchByAll(BoardSearchCondition searchCondition) {
        return emailContains(searchCondition.getEmail())
            .and(titleContains(searchCondition.getTitle()))
            .and(contentContains(searchCondition.getContent()))
            .and(statusEq(Status.ACTIVE))
            .and(categoryEq(searchCondition.getBoardCategory()));
    }

    private BooleanBuilder searchByAllForAdmin(BoardSearchCondition searchCondition) {
        return emailContains(searchCondition.getEmail())
            .and(titleContains(searchCondition.getTitle()))
            .and(contentContains(searchCondition.getContent()))
            .and(statusEq(searchCondition.getStatus()))
            .and(categoryEq(searchCondition.getBoardCategory()));
    }
}

/*
회원은 활성화 되어 있는 게시글만 볼 수 있고
관리자는 전부 볼 수 있게 하기 위해 메서드 나눠둠

searchByAll, searchByAllForAdmin 중복
검색 메서드 추가될 경우 중복 제거하기
 */