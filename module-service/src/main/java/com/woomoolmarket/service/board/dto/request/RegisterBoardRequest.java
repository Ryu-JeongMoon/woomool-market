package com.woomoolmarket.service.board.dto.request;

import com.woomoolmarket.model.board.entity.BoardCategory;
import com.woomoolmarket.model.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterBoardRequest {

    /* TODO member 를 그대로 넣지 말고 name 으로 치환해야 될 것 같음 */
    private Member member;

    private String title;

    private String content;

    private BoardCategory boardCategory;
}
