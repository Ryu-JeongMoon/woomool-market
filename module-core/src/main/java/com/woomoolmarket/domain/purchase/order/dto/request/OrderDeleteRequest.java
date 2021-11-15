package com.woomoolmarket.domain.purchase.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDeleteRequest {

    private Long memberId;
    private Long orderId;
}
