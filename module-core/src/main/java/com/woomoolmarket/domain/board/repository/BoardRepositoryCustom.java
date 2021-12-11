package com.woomoolmarket.domain.board.repository;

import com.woomoolmarket.domain.board.query.BoardQueryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    Page<BoardQueryResponse> searchBy(BoardSearchCondition condition, Pageable pageable);

    Page<BoardQueryResponse> searchByAdmin(BoardSearchCondition condition, Pageable pageable);
}
