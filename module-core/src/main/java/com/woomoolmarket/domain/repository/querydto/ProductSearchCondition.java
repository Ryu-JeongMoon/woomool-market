package com.woomoolmarket.domain.repository.querydto;

import com.woomoolmarket.domain.entity.enumeration.ProductCategory;
import com.woomoolmarket.domain.entity.enumeration.Region;
import com.woomoolmarket.domain.entity.enumeration.Status;
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
