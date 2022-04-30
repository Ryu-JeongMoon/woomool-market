package com.woomoolmarket.service.product.dto.response;

import com.woomoolmarket.domain.entity.enumeration.Region;
import com.woomoolmarket.domain.entity.enumeration.ProductCategory;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductResponse {

  private String name;

  private String description;

  private String productImage;

  private MemberResponse memberResponse;

  private Integer price;

  private Integer stock;

  private LocalDateTime createdDateTime;

  private ProductCategory productCategory;

  private Region region;
}
