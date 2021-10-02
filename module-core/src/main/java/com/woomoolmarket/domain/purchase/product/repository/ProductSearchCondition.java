package com.woomoolmarket.domain.purchase.product.repository;

import com.woomoolmarket.common.enumeration.Region;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.purchase.product.entity.ProductCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductSearchCondition {

    private String name;
    private String seller;
    private Region region;
    private Status status;
    private ProductCategory category;
}
