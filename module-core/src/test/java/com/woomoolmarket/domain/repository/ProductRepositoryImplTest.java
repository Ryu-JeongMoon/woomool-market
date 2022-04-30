package com.woomoolmarket.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.config.AbstractDataJpaTest;
import com.woomoolmarket.domain.entity.Product;
import com.woomoolmarket.domain.entity.enumeration.ProductCategory;
import com.woomoolmarket.domain.entity.enumeration.Region;
import com.woomoolmarket.domain.entity.enumeration.Status;
import com.woomoolmarket.domain.repository.querydto.ProductQueryResponse;
import com.woomoolmarket.domain.repository.querydto.ProductSearchCondition;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Slf4j
class ProductRepositoryImplTest extends AbstractDataJpaTest {

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

  @Nested
  @DisplayName("상품 조회")
  class FindProductTest {

    @Test
    @DisplayName("검색 값 없을시 기본 상태")
    void searchByDefaultCondition() {
      // given
      ProductSearchCondition searchCondition = ProductSearchCondition.builder()
        .build();

      // when
      Page<ProductQueryResponse> products = productRepository.searchByAdmin(searchCondition, Pageable.ofSize(10));

      // then
      assertThat(products.getTotalElements()).isEqualTo(3);
    }

    @Test
    @DisplayName("가격 범위")
    void searchByPriceRange() {
      // given
      ProductSearchCondition condition = ProductSearchCondition.builder()
        .minPrice(12000)
        .maxPrice(55000)
        .build();

      // when
      Page<ProductQueryResponse> page = productRepository.searchBy(condition, Pageable.ofSize(10));

      // then
      assertThat(page.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("지역")
    void searchByRegion() {
      // given
      ProductSearchCondition condition = ProductSearchCondition.builder()
        .region(Region.GANGWONDO)
        .build();

      // when
      Page<ProductQueryResponse> products = productRepository.searchByAdmin(condition, Pageable.ofSize(10));

      // then
      assertThat(products.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("상품 분류")
    void searchByCategory() {
      // given
      ProductSearchCondition condition = ProductSearchCondition.builder()
        .category(ProductCategory.FISH)
        .build();

      // when
      Page<ProductQueryResponse> products = productRepository.searchByAdmin(condition, Pageable.ofSize(10));

      // then
      assertThat(products.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("활성 상태")
    void searchByStatus() {
      // given
      ProductSearchCondition condition = ProductSearchCondition.builder()
        .status(Status.INACTIVE)
        .build();

      // when
      Page<ProductQueryResponse> products = productRepository.searchByAdmin(condition, Pageable.ofSize(10));

      // then
      assertThat(products.getTotalElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("상품명")
    void searchByName() {
      // given
      ProductSearchCondition condition = ProductSearchCondition.builder()
        .name("da3")
        .build();

      // when
      Page<ProductQueryResponse> queryResponsePage = productRepository.searchBy(condition, Pageable.ofSize(10));

      // then
      assertThat(queryResponsePage.getTotalElements()).isEqualTo(1);
    }
  }
}