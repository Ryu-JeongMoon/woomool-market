package com.woomoolmarket.service.board.dto.request;

import com.woomoolmarket.entity.board.entity.BoardCategory;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModifyBoardRequest {

    @NotBlank
    @Min(value = 2)
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private BoardCategory boardCategory;
}
