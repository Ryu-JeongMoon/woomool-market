package com.woomoolmarket.service.board.dto.response;

import com.woomoolmarket.domain.board.entity.BoardCategory;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
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

    private MemberResponse memberResponse;

    private String title;

    private String content;

    private int hit;

    private BoardCategory boardCategory;
}