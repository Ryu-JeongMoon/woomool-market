package com.woomoolmarket.domain.repository;


import static com.woomoolmarket.domain.entity.QBoard.board;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woomoolmarket.domain.entity.enumeration.BoardCategory;
import com.woomoolmarket.domain.entity.enumeration.Status;
import com.woomoolmarket.domain.repository.querydto.BoardQueryResponse;
import com.woomoolmarket.domain.repository.querydto.BoardSearchCondition;
import com.woomoolmarket.domain.repository.querydto.QBoardQueryResponse;
import com.woomoolmarket.util.CustomPageImpl;
import com.woomoolmarket.util.QueryDslUtils;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  private Page<BoardQueryResponse> searchTemplateBy(BooleanBuilder booleanBuilder, Pageable pageable) {
    QueryResults<BoardQueryResponse> results =
      queryFactory.select(
          new QBoardQueryResponse(
            board.id, board.title, board.content, board.hit, board.boardCount, board.boardCategory,
            board.member.email, board.endDateTime, board.startDateTime, board.createdDateTime))
        .from(board)
        .leftJoin(board.member)
        .leftJoin(board.boardCount)
        .where(booleanBuilder)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .orderBy(board.id.desc())
        .fetchResults();

    return new CustomPageImpl<>(results.getResults(), pageable.getPageNumber(), pageable.getPageSize(), results.getTotal());
  }

  @Override
  public Page<BoardQueryResponse> searchBy(BoardSearchCondition condition, Pageable pageable) {
    return searchTemplateBy(combineBy(condition), pageable);
  }

  @Override
  public Page<BoardQueryResponse> searchForAdminBy(BoardSearchCondition condition, Pageable pageable) {
    return searchTemplateBy(combineForAdminBy(condition), pageable);
  }

  private BooleanBuilder combineBy(BoardSearchCondition condition) {
    return emailContains(condition.getEmail())
      .and(titleContains(condition.getTitle()))
      .and(contentContains(condition.getContent()))
      .and(statusEquals(Status.ACTIVE))
      .and(categoryEquals(condition.getBoardCategory()))
      .and(board.startDateTime.before(LocalDateTime.now()))
      .and(board.endDateTime.after(LocalDateTime.now()));
  }

  private BooleanBuilder combineForAdminBy(BoardSearchCondition condition) {
    return emailContains(condition.getEmail())
      .and(titleContains(condition.getTitle()))
      .and(contentContains(condition.getContent()))
      .and(statusEquals(condition.getStatus()))
      .and(categoryEquals(condition.getBoardCategory()));
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

  private BooleanBuilder statusEquals(Status status) {
    return QueryDslUtils.nullSafeBuilder(() -> board.status.eq(status));
  }

  private BooleanBuilder categoryEquals(BoardCategory category) {
    return QueryDslUtils.nullSafeBuilder(() -> board.boardCategory.eq(category));
  }
}