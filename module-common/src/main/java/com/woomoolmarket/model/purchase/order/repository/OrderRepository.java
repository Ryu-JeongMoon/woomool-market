package com.woomoolmarket.model.purchase.order.repository;

import com.woomoolmarket.model.purchase.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
