package com.woomoolmarket.service.board.dto.request;

import com.woomoolmarket.domain.board.entity.BoardCategory;
import com.woomoolmarket.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardRequest {

    private Member member;
    private String title;
    private String content;
    private BoardCategory boardCategory;
}
