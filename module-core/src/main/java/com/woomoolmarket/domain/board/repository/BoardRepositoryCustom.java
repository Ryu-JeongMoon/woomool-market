package com.woomoolmarket.domain.board.repository;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.board.entity.Board;
import com.woomoolmarket.domain.board.query.BoardQueryResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    Page<BoardQueryResponse> findByConditionAndPage(BoardSearchCondition searchCondition, Pageable pageable);

    List<Board> findByConditionForAdmin(BoardSearchCondition searchCondition);

    Page<BoardQueryResponse> findByStatus(Pageable pageable, Status status);
}
