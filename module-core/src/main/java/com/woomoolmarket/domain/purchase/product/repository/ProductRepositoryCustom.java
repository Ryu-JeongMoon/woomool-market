package com.woomoolmarket.domain.purchase.product.repository;

import com.woomoolmarket.domain.purchase.product.query.ProductQueryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

  Page<ProductQueryResponse> searchBy(ProductSearchCondition condition, Pageable pageable);

  Page<ProductQueryResponse> searchByAdmin(ProductSearchCondition condition, Pageable pageable);
}
