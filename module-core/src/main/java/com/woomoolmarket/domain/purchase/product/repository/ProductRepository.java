package com.woomoolmarket.domain.purchase.product.repository;

import com.woomoolmarket.common.enumeration.Region;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.entity.ProductCategory;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.status = :status")
    List<Product> findProductsByStatusAndCache(@Param("status") Status status);

    Page<Product> findProductsByName(String name, Pageable pageable);

    Page<Product> findProductsBySeller(String seller, Pageable pageable);

    Page<Product> findProductsByRegion(Region region, Pageable pageable);

    Page<Product> findProductsByProductCategory(ProductCategory productCategory, Pageable pageable);

    @Query("select p from Product p where p.price between :minPrice and :maxPrice")
    Page<Product> findProductsByPriceRange(int minPrice, int maxPrice, Pageable pageable);

    Page<Product> findProductsByStatus(Status status, Pageable pageable);
}
