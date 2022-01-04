package com.woomoolmarket.domain.purchase.product.repository;

import com.woomoolmarket.common.enumeration.Region;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.purchase.product.entity.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchCondition {

  private String name;

  private String email;

  private Integer minPrice;

  private Integer maxPrice;

  private Region region;

  private Status status;

  private ProductCategory category;
}
