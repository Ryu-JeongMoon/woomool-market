package com.woomoolmarket.model.purchase.product.repository;

import com.woomoolmarket.model.purchase.product.entity.Category;
import com.woomoolmarket.model.purchase.product.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByName(String name);

    List<Product> findAllByCategory(Category category);

    @Query("select p from Product p where p.price between :minPrice and :maxPrice")
    List<Product> findAllByPrice(@Param("minPrice") Integer minPrice, @Param("maxPrice") Integer maxPrice);


}
