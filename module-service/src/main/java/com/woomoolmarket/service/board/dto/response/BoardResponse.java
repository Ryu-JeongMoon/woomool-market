package com.woomoolmarket.service.board.dto.response;

import com.woomoolmarket.domain.board.entity.BoardCategory;
import com.woomoolmarket.domain.count.entity.BoardCount;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BoardResponse implements Serializable {

    private Long id;

    private String title;

    private String content;

    private int hit;

    private BoardCount boardCount;

    private BoardCategory boardCategory;

    private LocalDateTime endDateTime;

    private LocalDateTime startDateTime;

    private LocalDateTime createdDateTime;

    private MemberResponse memberResponse;

}