package com.woomoolmarket.domain.purchase.cart.query;

import com.querydsl.core.annotations.QueryProjection;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.query.MemberQueryResponse;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.query.ProductQueryResponse;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartQueryResponse implements Serializable {

  private Long id;

  private MemberQueryResponse memberQueryResponse;

  private ProductQueryResponse productQueryResponse;

  private int quantity;

  @QueryProjection
  public CartQueryResponse(Long id, Member member, Product product, int quantity) {
    this.id = id;
    this.memberQueryResponse = MemberQueryResponse.of(member);
    this.productQueryResponse = ProductQueryResponse.of(product);
    this.quantity = quantity;
  }
}
