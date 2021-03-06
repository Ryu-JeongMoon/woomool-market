package com.woomoolmarket.domain.repository;

import com.woomoolmarket.domain.repository.querydto.OrderQueryResponse;
import com.woomoolmarket.domain.repository.querydto.OrderSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {

  Page<OrderQueryResponse> searchBy(Long memberId, Pageable pageable);

  Page<OrderQueryResponse> searchForAdminBy(OrderSearchCondition searchCondition, Pageable pageable);
}
