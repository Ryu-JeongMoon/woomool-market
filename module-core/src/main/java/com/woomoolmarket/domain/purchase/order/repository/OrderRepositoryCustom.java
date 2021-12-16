package com.woomoolmarket.domain.purchase.order.repository;

import com.woomoolmarket.domain.purchase.order.query.OrderQueryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {

    Page<OrderQueryResponse> searchBy(Long memberId, Pageable pageable);

    Page<OrderQueryResponse> searchForAdminBy(OrderSearchCondition searchCondition, Pageable pageable);
}
