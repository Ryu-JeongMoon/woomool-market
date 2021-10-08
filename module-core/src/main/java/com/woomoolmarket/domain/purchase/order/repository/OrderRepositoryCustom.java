package com.woomoolmarket.domain.purchase.order.repository;

import com.woomoolmarket.domain.purchase.order.entity.Order;
import java.util.List;

public interface OrderRepositoryCustom {

    List<Order> findByConditionForAdmin(OrderSearchCondition searchCondition);
}
