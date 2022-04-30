package com.woomoolmarket.domain.repository;

import com.woomoolmarket.domain.repository.querydto.ProductQueryResponse;
import com.woomoolmarket.domain.repository.querydto.ProductSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

  Page<ProductQueryResponse> searchBy(ProductSearchCondition condition, Pageable pageable);

  Page<ProductQueryResponse> searchByAdmin(ProductSearchCondition condition, Pageable pageable);
}
