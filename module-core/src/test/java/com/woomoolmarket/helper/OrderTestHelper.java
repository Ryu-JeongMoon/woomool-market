package com.woomoolmarket.helper;

import com.woomoolmarket.common.embeddable.Delivery;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.purchase.order.entity.Order;
import com.woomoolmarket.domain.purchase.order_product.entity.OrderProduct;
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
