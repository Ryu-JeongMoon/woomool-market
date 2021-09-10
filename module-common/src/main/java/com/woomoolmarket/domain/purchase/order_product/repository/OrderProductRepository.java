package com.woomoolmarket.domain.purchase.order_product.repository;

import com.woomoolmarket.domain.purchase.order_product.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

}
