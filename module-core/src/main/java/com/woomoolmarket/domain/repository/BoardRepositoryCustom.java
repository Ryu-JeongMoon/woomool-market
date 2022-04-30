package com.woomoolmarket.domain.repository;

import com.woomoolmarket.domain.repository.querydto.BoardQueryResponse;
import com.woomoolmarket.domain.repository.querydto.BoardSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

  Page<BoardQueryResponse> searchBy(BoardSearchCondition condition, Pageable pageable);

  Page<BoardQueryResponse> searchForAdminBy(BoardSearchCondition condition, Pageable pageable);
}
