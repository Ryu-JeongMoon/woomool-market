package com.woomoolmarket.service.board.dto.request;

import com.woomoolmarket.domain.entity.enumeration.BoardCategory;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BoardModifyRequest {

  @NotNull
  @Size(max = 255)
  private String title;

  @Lob
  @NotNull
  private String content;

  private BoardCategory boardCategory;
}
