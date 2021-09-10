package com.woomoolmarket.model.purchase.cart.repository;

import com.woomoolmarket.model.purchase.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
