package com.woomoolmarket.helper;

import com.woomoolmarket.common.enumeration.Region;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.entity.ProductCategory;

public class ProductTestHelper {

  public static final String PRODUCT_NAME = "panda";
  public static final String PRODUCT_DESCRIPTION = "panda is bear";
  public static final Integer PRODUCT_PRICE = 50000;
  public static final Integer PRODUCT_STOCK = 10000;
  public static final Region PRODUCT_REGION = Region.GANGWONDO;
  public static final ProductCategory PRODUCT_CATEGORY = ProductCategory.MEAT;

  public static Product createProduct(Member member) {
    return Product.builder()
      .member(member)
      .name(PRODUCT_NAME)
      .price(PRODUCT_PRICE)
      .stock(PRODUCT_STOCK)
      .region(PRODUCT_REGION)
      .description(PRODUCT_DESCRIPTION)
      .productCategory(PRODUCT_CATEGORY)
      .build();
  }
}
