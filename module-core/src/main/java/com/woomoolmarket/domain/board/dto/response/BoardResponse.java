package com.woomoolmarket.domain.board.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import com.woomoolmarket.domain.board.entity.BoardCategory;
import com.woomoolmarket.domain.member.dto.response.MemberResponse;
import java.time.LocalDateTime;
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

    private String title;

    private String content;

    private int hit;

    private BoardCategory boardCategory;

    private LocalDateTime endDateTime;

    private LocalDateTime startDateTime;

    private LocalDateTime createdDateTime;

    private MemberResponse memberResponse;

    @QueryProjection
    public BoardResponse(Long id, String title, String content, int hit, BoardCategory boardCategory, String email,
        LocalDateTime endDateTime, LocalDateTime startDateTime, LocalDateTime createdDateTime) {
        this.id = id;
        this.hit = hit;
        this.title = title;
        this.content = content;
        this.boardCategory = boardCategory;
        this.endDateTime = endDateTime;
        this.startDateTime = startDateTime;
        this.createdDateTime = createdDateTime;
        this.memberResponse = MemberResponse.from(email);
    }
}