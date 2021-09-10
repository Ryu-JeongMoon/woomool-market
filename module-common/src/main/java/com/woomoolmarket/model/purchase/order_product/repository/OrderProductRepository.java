package com.woomoolmarket.model.purchase.order_product.repository;

import com.woomoolmarket.model.purchase.order_product.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

}
