package com.woomoolmarket.helper;

import com.woomoolmarket.domain.enumeration.Region;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.entity.ProductCategory;
import com.woomoolmarket.domain.purchase.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductTestHelper {

  public static final String PRODUCT_NAME = "panda";
  public static final String PRODUCT_DESCRIPTION = "panda is bear";
  public static final Integer PRODUCT_PRICE = 50000;
  public static final Integer PRODUCT_STOCK = 10000;
  public static final Region PRODUCT_REGION = Region.GANGWONDO;
  public static final ProductCategory PRODUCT_CATEGORY = ProductCategory.MEAT;

  private final ProductRepository productRepository;

  public Product createProduct(Member member) {
    Product product = Product.builder()
      .member(member)
      .name(PRODUCT_NAME)
      .price(PRODUCT_PRICE)
      .stock(PRODUCT_STOCK)
      .description(PRODUCT_DESCRIPTION)
      .productCategory(PRODUCT_CATEGORY)
      .region(PRODUCT_REGION)
      .build();
    return productRepository.save(product);
  }
}
