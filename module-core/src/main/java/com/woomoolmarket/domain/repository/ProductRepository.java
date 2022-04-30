package com.woomoolmarket.domain.repository;

import com.woomoolmarket.domain.entity.Product;
import com.woomoolmarket.domain.entity.enumeration.Status;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

  Optional<Product> findByIdAndStatus(Long id, Status status);

  Optional<Product> findByName(String name);
}
