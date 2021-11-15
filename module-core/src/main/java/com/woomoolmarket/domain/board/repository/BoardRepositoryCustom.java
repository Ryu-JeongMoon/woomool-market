package com.woomoolmarket.domain.board.repository;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.board.dto.response.BoardResponse;
import com.woomoolmarket.domain.board.entity.Board;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    Page<BoardResponse> findByConditionAndPage(BoardSearchCondition searchCondition, Pageable pageable);

    List<Board> findByConditionForAdmin(BoardSearchCondition searchCondition);

    Page<BoardResponse> findByStatus(Pageable pageable, Status status);
}
