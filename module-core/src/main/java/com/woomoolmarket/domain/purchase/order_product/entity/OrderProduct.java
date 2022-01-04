package com.woomoolmarket.domain.purchase.order_product.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.woomoolmarket.common.auditing.BaseTimeEntity;
import com.woomoolmarket.domain.purchase.order.entity.Order;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ORDER_PRODUCT")
@ToString(exclude = "order")
@EqualsAndHashCode(of = "id", callSuper = false)
public class OrderProduct extends BaseTimeEntity {

  @Id
  @Column(name = "order_product_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonBackReference
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private Order order;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "product_id")
  private Product product;

  @Column(nullable = false)
  private int quantity;

  @Column(nullable = false)
  private int totalPrice;

  @Builder
  public OrderProduct(Product product, int quantity) {
    this.product = product;
    this.quantity = quantity;
    this.totalPrice = product.getPrice() * quantity;
  }

  public static OrderProduct createOrderProduct(Product product, int quantity) {
    product.decreaseStock(quantity);

    return OrderProduct.builder()
      .product(product)
      .quantity(quantity)
      .build();
  }

  public void cancelOrder() {
    product.increaseStock(quantity);
  }
}

// TODO product Fetch.EAGER 로 해놨는데 개선해야 하는걸까?