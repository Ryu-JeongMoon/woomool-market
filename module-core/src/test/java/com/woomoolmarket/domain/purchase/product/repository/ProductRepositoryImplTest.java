package com.woomoolmarket.domain.purchase.product.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.domain.enumeration.Region;
import com.woomoolmarket.domain.enumeration.Status;
import com.woomoolmarket.config.TestConfig;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.entity.ProductCategory;
import com.woomoolmarket.domain.purchase.product.query.ProductQueryResponse;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Log4j2
@DataJpaTest
@Import(TestConfig.class)
class ProductRepositoryImplTest {

  @Autowired
  ProductRepository productRepository;

  @BeforeEach
  void init() {
    Product product1 = Product.builder()
      .name("panda1")
      .description("DESC")
      .price(15000)
      .stock(500)
      .region(Region.GANGWONDO)
      .productCategory(ProductCategory.FISH)
      .build();

    Product product2 = Product.builder()
      .name("panda2")
      .description("DESC")
      .price(35000)
      .stock(500)
      .region(Region.CHUNGCHEONGBUKDO)
      .productCategory(ProductCategory.MEAT)
      .build();

    Product product3 = Product.builder()
      .name("panda3")
      .description("DESC")
      .price(56000)
      .stock(500)
      .region(Region.GYEONGGIDO)
      .productCategory(ProductCategory.VEGETABLE)
      .build();

    productRepository.saveAll(List.of(product1, product2, product3));
  }

  @Test
  @DisplayName("범위 값 동적 쿼리 조회")
  void priceRangeTest() {
    ProductSearchCondition condition = ProductSearchCondition.builder()
      .minPrice(12000)
      .maxPrice(55000)
      .build();
    Page<ProductQueryResponse> page = productRepository.searchBy(condition, Pageable.ofSize(10));
    assertThat(page.getTotalElements()).isEqualTo(2);
  }

  @Test
  @DisplayName("모든 검색 값 동적 쿼리 조회")
  void searchByAllConditions() {
    ProductSearchCondition searchCondition = ProductSearchCondition.builder()
      .build();

    Page<ProductQueryResponse> products = productRepository.searchByAdmin(searchCondition, Pageable.ofSize(10));
    assertThat(products.getTotalElements()).isEqualTo(3);
  }

  @Test
  @DisplayName("지역 검색")
  void searchByRegion() {
    ProductSearchCondition condition = ProductSearchCondition.builder()
      .region(Region.GANGWONDO)
      .build();
    Page<ProductQueryResponse> products = productRepository.searchByAdmin(condition, Pageable.ofSize(10));
    assertThat(products.getTotalElements()).isEqualTo(1);
  }

  @Test
  @DisplayName("분류 검색")
  void searchByCategory() {
    ProductSearchCondition condition = ProductSearchCondition.builder()
      .category(ProductCategory.FISH)
      .build();
    Page<ProductQueryResponse> products = productRepository.searchByAdmin(condition, Pageable.ofSize(10));
    assertThat(products.getTotalElements()).isEqualTo(1);
  }

  @Test
  @DisplayName("활성 상태 검색")
  void searchByStatus() {
    ProductSearchCondition condition = ProductSearchCondition.builder()
      .status(Status.INACTIVE)
      .build();
    Page<ProductQueryResponse> products = productRepository.searchByAdmin(condition, Pageable.ofSize(10));
    assertThat(products.getTotalElements()).isEqualTo(0);
  }

  @Test
  @DisplayName("상품명 검색 페이지 형식")
  void findTemplate() {
    ProductSearchCondition condition = ProductSearchCondition.builder()
      .name("da3")
      .build();
    Page<ProductQueryResponse> queryResponsePage = productRepository.searchBy(condition, Pageable.ofSize(10));
    assertThat(queryResponsePage.getTotalElements()).isEqualTo(1);
  }

  @Test
  @DisplayName("지역 검색 페이지 형식")
  void findPageByCondition() {
    ProductSearchCondition condition = ProductSearchCondition.builder()
      .region(Region.GYEONGGIDO)
      .build();
    Page<ProductQueryResponse> page = productRepository.searchBy(condition, Pageable.ofSize(10));
    assertThat(page.getTotalElements()).isEqualTo(1);
  }

  @Test
  @DisplayName("상태 검색 페이지 형식")
  void findPageByConditionForAdmin() {
    ProductSearchCondition condition = ProductSearchCondition.builder()
      .status(Status.INACTIVE)
      .build();
    Page<ProductQueryResponse> page = productRepository.searchByAdmin(condition, Pageable.ofSize(10));
    assertThat(page.getTotalElements()).isEqualTo(0);
  }
}