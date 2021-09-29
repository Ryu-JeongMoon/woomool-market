package com.woomoolmarket.domain.purchase.order.repository;

import com.woomoolmarket.domain.purchase.order.entity.Order;
import com.woomoolmarket.domain.purchase.order.entity.OrderStatus;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findOrdersByOrderStatus(OrderStatus status, Pageable pageable);

    @Query("select o from Order o where o.member = :memberId and o.status in ('ONGOING', 'DELIVERED')")
    Page<Order> findOrdersByMemberId(Long memberId, Pageable pageable);
}
