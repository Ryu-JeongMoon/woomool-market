package com.woomoolmarket.service.board.dto.request;

import com.woomoolmarket.domain.board.entity.BoardCategory;
import javax.persistence.Lob;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardModifyRequest {

    @NotBlank
    @Size(min = 2, max = 255)
    private String title;

    @Lob
    @NotBlank
    private String content;

    @NotBlank
    private BoardCategory boardCategory;
}
