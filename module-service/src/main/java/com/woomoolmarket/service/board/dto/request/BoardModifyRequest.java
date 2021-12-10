package com.woomoolmarket.service.board.dto.request;

import com.woomoolmarket.domain.board.entity.BoardCategory;
import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @NotNull
    @Size(max = 255)
    private String title;

    @Lob
    @NotNull
    private String content;

    private BoardCategory boardCategory;
}
