package com.woomoolmarket.domain.purchase.cart.repository;

import com.woomoolmarket.domain.purchase.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
