package com.woomoolmarket.service.board.dto.response;

import com.woomoolmarket.entity.board.entity.BoardCategory;
import com.woomoolmarket.entity.member.entity.Member;
import com.woomoolmarket.entity.reply.entity.Reply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponse {

    private Long id;
    private Member member;
    private List<Reply> replies = new ArrayList<>();
    private String title;
    private String content;
    private int hit;
    private BoardCategory boardCategory;
}
