package com.woomoolmarket.service.board.dto.response;

import com.woomoolmarket.domain.board.entity.BoardCategory;
import com.woomoolmarket.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponse {

    private Long id;

    private Member member;

    private String title;

    private String content;

    private int hit;

    private BoardCategory boardCategory;
}