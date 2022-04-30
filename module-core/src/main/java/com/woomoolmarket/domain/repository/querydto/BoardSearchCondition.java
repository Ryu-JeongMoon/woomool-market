package com.woomoolmarket.domain.repository.querydto;

import com.woomoolmarket.domain.entity.enumeration.BoardCategory;
import com.woomoolmarket.domain.entity.enumeration.Status;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardSearchCondition {

  private String email;

  private String title;

  private String content;

  @Enumerated(EnumType.STRING)
  private Status status;

  @Enumerated(EnumType.STRING)
  private BoardCategory boardCategory;

  @Builder
  public BoardSearchCondition(String email, String title, String content, Status status, BoardCategory boardCategory) {
    this.email = email;
    this.title = title;
    this.content = content;
    this.status = status != null ? status : Status.ACTIVE;
    this.boardCategory = boardCategory;
  }
}
