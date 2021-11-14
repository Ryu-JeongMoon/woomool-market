package com.woomoolmarket.domain.board.dto.request;

import com.woomoolmarket.domain.board.entity.BoardCategory;
import javax.persistence.Lob;
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

    @Size(min = 2, max = 255)
    private String title;

    @Lob
    private String content;

    private BoardCategory boardCategory;
}
