package com.woomoolmarket.service.cart.dto.response;

import com.woomoolmarket.service.member.dto.response.MemberResponse;
import com.woomoolmarket.service.product.dto.response.ProductResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartResponse {

    private Long id;
    private MemberResponse memberResponse;
    private ProductResponse productResponse;
    private Integer quantity;
}
