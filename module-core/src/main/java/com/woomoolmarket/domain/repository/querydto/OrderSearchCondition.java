package com.woomoolmarket.domain.repository.querydto;

import com.woomoolmarket.domain.entity.enumeration.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchCondition {

  private Long memberId;

  private String email;

  private OrderStatus orderStatus;
}
