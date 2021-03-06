package com.woomoolmarket.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.woomoolmarket.domain.entity.auditing.BaseTimeEntity;
import com.woomoolmarket.domain.entity.embeddable.Delivery;
import com.woomoolmarket.domain.entity.enumeration.OrderStatus;
import com.woomoolmarket.util.constants.ExceptionMessages;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ORDERS")
@EqualsAndHashCode(of = "id", callSuper = false)
public class Order extends BaseTimeEntity {

  @Id
  @Column(name = "order_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "status", nullable = false, length = 50)
  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus = OrderStatus.ONGOING;

  @JoinColumn(name = "member_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  @JsonManagedReference
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  private List<OrderProduct> orderProducts = new ArrayList<>();

  @Embedded
  private Delivery delivery;

  @Builder
  public Order(Member member, Delivery delivery, List<OrderProduct> orderProducts) {
    this.member = member;
    this.delivery = delivery;
    this.addOrderProducts(orderProducts);
  }

  public void addOrderProducts(List<OrderProduct> orderProducts) {
    if (orderProducts == null || orderProducts.isEmpty()) {
      throw new EntityNotFoundException(ExceptionMessages.Cart.NOT_FOUND);
    }

    this.orderProducts.addAll(orderProducts);
    orderProducts.forEach(o -> o.setOrder(this));
  }

  public void cancel() {
    if (orderStatus == OrderStatus.DELIVERED) {
      throw new IllegalArgumentException(ExceptionMessages.Order.CANNOT_CANCEL);
    }

    orderStatus = OrderStatus.CANCELED;
    orderProducts.forEach(OrderProduct::cancelOrder);
  }
}

/*
?????? ?????? ?????? ?????? ??????
1. @JsonIgnore ??????
2. @JsonManagedReference & @JsonBackReference ??????

??? ?????? ?????? ??????????????? ?????? ??? ????????? ?????????????????? ????????? ??????.
@JsonIgnore ??????, property null ??????
@JsonManagedReference & @JsonBackReference ??????????????? ?????? ?????? Annotation
 */