package com.woomoolmarket.domain.board.repository;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.board.entity.BoardCategory;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardSearchCondition {

    private String email;
    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private Status status;
    private BoardCategory boardCategory;

    @Builder
    public BoardSearchCondition(String email, String title, String content, Status status, BoardCategory boardCategory) {
        this.email = email;
        this.title = title;
        this.content = content;
        this.status = status != null ? status : Status.ACTIVE;
        this.boardCategory = boardCategory;
    }
}
