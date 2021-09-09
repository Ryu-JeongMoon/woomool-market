package com.woomoolmarket.entity.purchase.order.repository;

import com.woomoolmarket.entity.purchase.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
