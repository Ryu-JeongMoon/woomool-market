package com.woomoolmarket.service.board.dto.request;

import com.woomoolmarket.domain.board.entity.BoardCategory;
import javax.persistence.Lob;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    @NotBlank
    private BoardCategory boardCategory;
}
