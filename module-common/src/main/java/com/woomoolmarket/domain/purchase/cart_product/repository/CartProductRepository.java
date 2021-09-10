package com.woomoolmarket.domain.purchase.cart_product.repository;

import com.woomoolmarket.domain.purchase.cart_product.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {

}
