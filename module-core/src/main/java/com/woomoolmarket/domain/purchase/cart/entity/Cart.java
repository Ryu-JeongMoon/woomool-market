package com.woomoolmarket.domain.purchase.cart.entity;

import com.woomoolmarket.domain.auditing.BaseTimeEntity;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class Cart extends BaseTimeEntity {

  @Id
  @Column(name = "cart_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JoinColumn(name = "member_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  @JoinColumn(name = "product_id")
  @OneToOne(fetch = FetchType.LAZY)
  private Product product;

  @Column(nullable = false)
  private int quantity;

  @Builder
  public Cart(Member member, Product product, int quantity) {
    this.member = member;
    this.product = product;
    this.quantity = quantity;
  }
}
