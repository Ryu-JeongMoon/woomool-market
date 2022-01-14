package com.woomoolmarket.helper;

import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.purchase.cart.entity.Cart;
import com.woomoolmarket.domain.purchase.cart.repository.CartRepository;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CartTestHelper {

  public static final int CART_QUANTITY = 500;

  private final CartRepository cartRepository;

  public Cart get(Member member, Product product) {
    return create(member, product, CART_QUANTITY);
  }

  public Cart create(Member member, Product product, int quantity) {
    Cart cart = Cart.builder()
      .member(member)
      .product(product)
      .quantity(quantity)
      .build();
    return cartRepository.save(cart);
  }
}
