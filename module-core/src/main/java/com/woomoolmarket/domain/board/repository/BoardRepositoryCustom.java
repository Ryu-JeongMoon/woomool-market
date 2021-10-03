package com.woomoolmarket.domain.board.repository;

import com.woomoolmarket.domain.board.entity.Board;
import java.util.List;

public interface BoardRepositoryCustom {

    List<Board> findByCondition(BoardSearchCondition searchCondition);

    List<Board> findByConditionForAdmin(BoardSearchCondition searchCondition);

}
