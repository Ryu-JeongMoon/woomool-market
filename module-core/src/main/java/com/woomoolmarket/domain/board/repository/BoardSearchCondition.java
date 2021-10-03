package com.woomoolmarket.domain.board.repository;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.board.entity.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardSearchCondition {

    private String email;
    private String title;
    private String content;
    private Status status;
    private BoardCategory boardCategory;
}
