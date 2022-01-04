package com.woomoolmarket.domain.purchase.order.repository;

import com.woomoolmarket.domain.purchase.order.entity.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

  @Query("select o from Order o join fetch o.member where o.member.id = :memberId and o.orderStatus in ('ONGOING', 'DELIVERED')")
  List<Order> findByMemberId(Long memberId);
}
