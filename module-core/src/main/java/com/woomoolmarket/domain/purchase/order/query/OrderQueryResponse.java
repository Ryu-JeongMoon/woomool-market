package com.woomoolmarket.domain.purchase.order.query;

import com.querydsl.core.annotations.QueryProjection;
import com.woomoolmarket.common.embeddable.Delivery;
import com.woomoolmarket.domain.purchase.order.entity.OrderStatus;
import com.woomoolmarket.domain.purchase.order_product.entity.OrderProduct;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderQueryResponse implements Serializable {

    private Long id;

    private OrderStatus orderStatus;

    private String email;

    private List<OrderProduct> orderProducts;

    private Delivery delivery;

    @QueryProjection
    public OrderQueryResponse(Long id, String email, Delivery delivery, OrderStatus orderStatus,
        List<OrderProduct> orderProducts) {

        this.id = id;
        this.email = email;
        this.delivery = delivery;
        this.orderStatus = orderStatus;
        this.orderProducts = orderProducts;
    }
}
