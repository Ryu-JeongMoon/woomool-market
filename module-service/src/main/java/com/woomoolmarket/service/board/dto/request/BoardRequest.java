package com.woomoolmarket.service.board.dto.request;

import com.woomoolmarket.domain.entity.enumeration.BoardCategory;
import java.time.LocalDateTime;
import javax.persistence.Lob;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BoardRequest {

  @Email
  @NotBlank
  @Size(min = 9, max = 64)
  @Pattern(regexp = "(?i)^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$")
  private String email;

  @Size(max = 255)
  @NotBlank
  private String title;

  @Lob
  @NotBlank
  private String content;

  private BoardCategory boardCategory;

  private LocalDateTime startDateTime;

  private LocalDateTime endDateTime;
}
