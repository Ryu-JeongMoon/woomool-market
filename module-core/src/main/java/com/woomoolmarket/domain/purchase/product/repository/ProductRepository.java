package com.woomoolmarket.domain.purchase.product.repository;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.board.entity.Board;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    Optional<Product> findByIdAndStatus(Long id, Status status);


}
