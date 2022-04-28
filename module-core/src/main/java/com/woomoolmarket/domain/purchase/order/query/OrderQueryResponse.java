package com.woomoolmarket.domain.purchase.order.query;

import com.querydsl.core.annotations.QueryProjection;
import com.woomoolmarket.domain.embeddable.Delivery;
import com.woomoolmarket.domain.purchase.order.entity.Order;
import com.woomoolmarket.domain.purchase.order.entity.OrderStatus;
import com.woomoolmarket.domain.purchase.order_product.entity.OrderProduct;
import java.io.Serializable;
import java.time.LocalDateTime;
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

  private String email;

  private Delivery delivery;

  private OrderStatus orderStatus;

  private LocalDateTime createdDateTime;

  private LocalDateTime lastModifiedDateTime;

  private List<OrderProduct> orderProducts;

  @QueryProjection
  public OrderQueryResponse(Order order) {
    this.id = order.getId();
    this.email = order.getMember().getEmail();
    this.delivery = order.getDelivery();
    this.orderStatus = order.getOrderStatus();
    this.orderProducts = order.getOrderProducts();
    this.createdDateTime = order.getCreatedDateTime();
    this.lastModifiedDateTime = order.getLastModifiedDateTime();
  }
}
