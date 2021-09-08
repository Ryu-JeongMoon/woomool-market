package com.woomoolmarket.entity.purchase.cart.repository;

import com.woomoolmarket.entity.purchase.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
