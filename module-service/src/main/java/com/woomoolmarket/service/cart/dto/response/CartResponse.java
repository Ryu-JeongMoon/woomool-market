package com.woomoolmarket.service.cart.dto.response;

import com.woomoolmarket.service.member.dto.response.MemberResponse;
import com.woomoolmarket.service.product.dto.response.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CartResponse {

  private Long id;

  private int quantity;

  private MemberResponse memberResponse;

  private ProductResponse productResponse;

}
