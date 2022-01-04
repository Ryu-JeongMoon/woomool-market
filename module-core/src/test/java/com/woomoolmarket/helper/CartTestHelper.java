package com.woomoolmarket.helper;

import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.purchase.cart.entity.Cart;
import com.woomoolmarket.domain.purchase.product.entity.Product;

public class CartTestHelper {

  public static final Integer CART_QUANTITY = 500;

  public static Cart createCart(Member member, Product product) {
    return Cart.builder()
      .member(member)
      .product(product)
      .quantity(CART_QUANTITY)
      .build();
  }
}
