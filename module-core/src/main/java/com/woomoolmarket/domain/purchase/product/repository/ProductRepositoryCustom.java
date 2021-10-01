package com.woomoolmarket.domain.purchase.product.repository;

import com.woomoolmarket.domain.purchase.product.entity.Product;
import java.util.List;

public interface ProductRepositoryCustom {

    List<Product> findByPriceRange(Integer minPrice, Integer maxPrice);

    List<Product> findByCondition(ProductSearchCondition searchCondition);
}
