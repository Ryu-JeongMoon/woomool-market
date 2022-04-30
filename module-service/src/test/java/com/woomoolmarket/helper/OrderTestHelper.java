package com.woomoolmarket.helper;

import com.woomoolmarket.domain.entity.embeddable.Delivery;
import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.entity.Order;
import com.woomoolmarket.domain.repository.OrderRepository;
import com.woomoolmarket.domain.entity.OrderProduct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderTestHelper {

  private final OrderRepository orderRepository;

  public Order createOrder(Member member, OrderProduct orderProduct) {
    Order order = Order.builder()
      .member(member)
      .orderProducts(List.of(orderProduct))
      .delivery(new Delivery(member.getEmail(), member.getPhone(), member.getAddress()))
      .build();
    return orderRepository.save(order);
  }
}
