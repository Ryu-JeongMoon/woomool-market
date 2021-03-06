package com.woomoolmarket.domain.entity;


import com.woomoolmarket.domain.entity.auditing.BaseTimeEntity;
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

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "CART_PRODUCT")
@EqualsAndHashCode(of = "id", callSuper = false)
public class CartProduct extends BaseTimeEntity {

  @Id
  @Column(name = "cart_product_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cart_id")
  private Cart cart;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  private Product product;

  @Column(nullable = false)
  private int amount;

  @Column(nullable = false)
  private int quantity;

  @Builder
  public CartProduct(Cart cart, Product product, int amount, int quantity) {
    this.cart = cart;
    this.product = product;
    this.amount = amount;
    this.quantity = quantity;
  }
}
