package com.woomoolmarket.domain.purchase.product.repository;

import com.woomoolmarket.domain.purchase.product.entity.ProductCategory;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByName(String name);

    List<Product> findAllByProductCategory(ProductCategory productCategory);

    @Query("select p from Product p where p.price between :minPrice and :maxPrice")
    List<Product> findAllByPriceRange(@Param("minPrice") int minPrice, @Param("maxPrice") int maxPrice);


}
