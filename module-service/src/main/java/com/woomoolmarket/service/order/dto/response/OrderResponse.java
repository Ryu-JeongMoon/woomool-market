package com.woomoolmarket.service.order.dto.response;

import com.woomoolmarket.common.embeddable.Delivery;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.purchase.order.entity.OrderStatus;
import com.woomoolmarket.domain.purchase.order_product.entity.OrderProduct;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private Member member;
    private OrderStatus orderStatus;
    private List<OrderProduct> orderProducts = new ArrayList<>();
    private Delivery delivery;
}
