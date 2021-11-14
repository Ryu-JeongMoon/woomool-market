package com.woomoolmarket.domain.purchase.order.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @NotNull
    @Min(value = 1L)
    private Long memberId;

    @Min(value = 1L)
    private Long productId;

    @NotNull
    @Min(value = 1L)
    private Integer quantity;
}
