package com.woomoolmarket.helper;

import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.entity.Order;
import com.woomoolmarket.domain.entity.OrderProduct;
import com.woomoolmarket.domain.entity.embeddable.Delivery;
import java.util.List;

public class OrderTestHelper {

  public static Order createOrder(Member member, OrderProduct orderProduct) {
    return Order.builder()
      .member(member)
      .orderProducts(List.of(orderProduct))
      .delivery(new Delivery(member.getEmail(), member.getPhone(), member.getAddress()))
      .build();
  }
}
