package com.woomoolmarket.service.order.dto.response;

import com.woomoolmarket.domain.entity.embeddable.Delivery;
import com.woomoolmarket.domain.entity.enumeration.OrderStatus;
import com.woomoolmarket.domain.entity.OrderProduct;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OrderResponse {

  private Long id;
  private MemberResponse memberResponse;
  private OrderStatus orderStatus;
  private List<OrderProduct> orderProducts;
  private Delivery delivery;
}
