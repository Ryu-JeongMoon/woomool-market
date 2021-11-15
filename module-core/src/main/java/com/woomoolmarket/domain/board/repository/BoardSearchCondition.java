package com.woomoolmarket.domain.board.repository;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.board.entity.BoardCategory;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardSearchCondition {

    private String email;
    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;
    private BoardCategory boardCategory;
}
