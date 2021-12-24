package com.woomoolmarket.domain.board.query;

import com.querydsl.core.annotations.QueryProjection;
import com.woomoolmarket.domain.board.entity.BoardCategory;
import com.woomoolmarket.domain.count.entity.BoardCount;
import com.woomoolmarket.domain.member.query.MemberQueryResponse;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardQueryResponse implements Serializable {

    private Long id;

    private String title;

    private String content;

    private int hit;

    private BoardCount boardCount;

    private BoardCategory boardCategory;

    private LocalDateTime endDateTime;

    private LocalDateTime startDateTime;

    private LocalDateTime createdDateTime;

    private MemberQueryResponse memberResponse;

    @QueryProjection
    public BoardQueryResponse(Long id, String title, String content, int hit, BoardCount boardCount,
        BoardCategory boardCategory, String email,
        LocalDateTime endDateTime, LocalDateTime startDateTime, LocalDateTime createdDateTime) {
        this.id = id;
        this.hit = hit;
        this.title = title;
        this.content = content;
        this.boardCount = boardCount;
        this.boardCategory = boardCategory;
        this.endDateTime = endDateTime;
        this.startDateTime = startDateTime;
        this.createdDateTime = createdDateTime;
        this.memberResponse = MemberQueryResponse.from(email);
    }
}
