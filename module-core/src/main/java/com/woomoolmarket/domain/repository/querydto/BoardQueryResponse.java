package com.woomoolmarket.domain.repository.querydto;

import com.querydsl.core.annotations.QueryProjection;
import com.woomoolmarket.domain.entity.BoardCount;
import com.woomoolmarket.domain.entity.enumeration.BoardCategory;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardQueryResponse implements Serializable {

  private final Long id;

  private final String title;

  private final String content;

  private final int hit;

  private final BoardCount boardCount;

  private final BoardCategory boardCategory;

  private final LocalDateTime endDateTime;

  private final LocalDateTime startDateTime;

  private final LocalDateTime createdDateTime;

  private final MemberQueryResponse memberResponse;

  @Builder
  @QueryProjection
  public BoardQueryResponse(
    Long id, String title, String content, int hit, BoardCount boardCount, BoardCategory boardCategory, String email,
    LocalDateTime endDateTime, LocalDateTime startDateTime, LocalDateTime createdDateTime) {
    this.id = id;
    this.hit = hit;
    this.title = title;
    this.content = content;
    this.boardCount = boardCount;
    this.boardCategory = boardCategory;
    this.endDateTime = endDateTime;
    this.startDateTime = startDateTime;
    this.createdDateTime = createdDateTime;
    this.memberResponse = MemberQueryResponse.from(email);
  }
}
