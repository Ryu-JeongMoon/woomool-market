package com.woomoolmarket.service.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OrderDeleteRequest {

    private Long memberId;
    private Long orderId;
}
