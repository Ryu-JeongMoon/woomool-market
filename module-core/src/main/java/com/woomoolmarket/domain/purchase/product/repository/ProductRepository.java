package com.woomoolmarket.domain.purchase.product.repository;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    List<Product> findByStatus(Status status);

}
