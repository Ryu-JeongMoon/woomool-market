package com.woomoolmarket.service.product;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.config.AbstractServiceTest;
import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.entity.Product;
import com.woomoolmarket.domain.entity.enumeration.ProductCategory;
import com.woomoolmarket.domain.entity.enumeration.Region;
import com.woomoolmarket.domain.entity.enumeration.Status;
import com.woomoolmarket.domain.repository.querydto.ProductSearchCondition;
import com.woomoolmarket.domain.repository.querydto.ProductQueryResponse;
import com.woomoolmarket.service.product.dto.request.ProductModifyRequest;
import com.woomoolmarket.service.product.dto.request.ProductRequest;
import com.woomoolmarket.service.product.dto.response.ProductResponse;
import com.woomoolmarket.service.product.mapper.ProductResponseMapper;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j
class ProductServiceTest extends AbstractServiceTest {

  private static Long PRODUCT1_ID;
  private static Long PRODUCT2_ID;

  @Autowired
  ProductResponseMapper productResponseMapper;

  @BeforeEach
  void init() {
    Member member = memberTestHelper.createMember();
    MEMBER_ID = member.getId();

    Product product1 = productTestHelper.createProduct(member);
    PRODUCT1_ID = product1.getId();

    Product product2 = Product.builder()
      .name("tiger")
      .member(member)
      .price(30000)
      .stock(5000)
      .productCategory(ProductCategory.FISH)
      .description("tiger cat")
      .region(Region.GANGWONDO)
      .build();
    PRODUCT2_ID = productRepository.save(product2).getId();
  }

  @AfterEach
  void clear() {
    em.createNativeQuery("ALTER TABLE PRODUCT ALTER COLUMN `product_id` RESTART WITH 1").executeUpdate();
    Objects.requireNonNull(stringRedisTemplate.keys("*")).forEach(k -> stringRedisTemplate.delete(k));
  }

  @Test
  @DisplayName("상품 번호로 조회")
  void getByIdAndStatus() {
    ProductResponse productResponse = productService.findBy(PRODUCT1_ID, Status.ACTIVE);
    assertThat(productResponse.getPrice()).isEqualTo(50000);
  }

  @Test
  @DisplayName("검색 조건 - 이름으로 조회")
  void getListBySearchConditionForMember() {
    ProductSearchCondition condition = ProductSearchCondition.builder()
      .name("an")
      .build();
    Page<ProductQueryResponse> products = productService.searchBy(condition, Pageable.ofSize(10));
    assertThat(products.getTotalElements()).isEqualTo(1);
  }

  @Test
  @DisplayName("검색 조건 - 카테고리로 조회")
  void getListBySearchConditionForMember2() {
    ProductSearchCondition condition = ProductSearchCondition.builder()
      .category(ProductCategory.FISH)
      .build();
    Page<ProductQueryResponse> products = productService.searchBy(condition, Pageable.ofSize(10));
    assertThat(products.getTotalElements()).isEqualTo(1);
  }

  @Test
  @DisplayName("상품 생성 성공")
  void create() {
    ProductRequest productRequest = ProductRequest.builder()
      .name("dog")
      .price(15000)
      .stock(900)
      .description("mung mung")
      .region(Region.CHUNGCHEONGBUKDO)
      .productCategory(ProductCategory.CEREAL).build();
    productService.create(productRequest);

    Product product = productRepository.findByName("dog").get();
    assertThat(product.getId()).isNotNull();
  }

  @Test
  @DisplayName("가격 변경")
  void editPrice() {
    int newPrice = 90000;
    ProductModifyRequest productModifyRequest = ProductModifyRequest.builder()
      .price(newPrice)
      .build();

    productService.edit(PRODUCT1_ID, productModifyRequest);

    Product product = productRepository.findById(PRODUCT1_ID).get();
    assertThat(product.getPrice()).isEqualTo(newPrice);
  }

  @Test
  @DisplayName("재고 변경")
  void editStock() {
    int newStock = 500000;
    ProductModifyRequest productModifyRequest = ProductModifyRequest.builder()
      .stock(newStock)
      .build();
    productService.edit(PRODUCT1_ID, productModifyRequest);
    Product product = productRepository.findById(PRODUCT1_ID).get();
    assertThat(product.getStock().intValue()).isEqualTo(newStock);
  }

  @Test
  @DisplayName("삭제 시 INACTIVE, DeletedDateTime")
  void deleteSoftly() {
    productService.deleteSoftly(PRODUCT1_ID);
    Product product = productRepository.findById(PRODUCT1_ID).get();

    assertThat(product.getStatus()).isEqualTo(Status.INACTIVE);
    assertThat(product.getDeletedDateTime()).isNotNull();
  }

  @Test
  @DisplayName("검색 조건 - 판매자 이름으로 조회")
  void getListBySearchConditionForAdmin() {
    ProductSearchCondition condition = ProductSearchCondition.builder()
      .email("pa")
      .build();

    Page<ProductQueryResponse> products = productService.searchByAdmin(condition, Pageable.ofSize(10));
    assertThat(products.getTotalElements()).isEqualTo(2);
  }
}