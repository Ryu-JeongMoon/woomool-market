package com.woomoolmarket.domain.purchase.product.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.common.enumeration.Region;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.config.TestConfig;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.entity.ProductCategory;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

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
        List<Product> products = productRepository.findByPriceRange(12000, 55000);
        assertThat(products.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("모든 검색 값 동적 쿼리 조회")
    void searchByAllConditions() {
        ProductSearchCondition searchCondition = ProductSearchCondition.builder()
            .build();

        List<Product> products = productRepository.findByConditionForAdmin(searchCondition);
        assertThat(products.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("지역 검색")
    void searchByRegion() {
        ProductSearchCondition condition = ProductSearchCondition.builder()
            .region(Region.GANGWONDO)
            .build();
        List<Product> products = productRepository.findByConditionForAdmin(condition);
        assertThat(products.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("분류 검색")
    void searchByCategory() {
        ProductSearchCondition condition = ProductSearchCondition.builder()
            .category(ProductCategory.FISH)
            .build();
        List<Product> products = productRepository.findByConditionForAdmin(condition);
        assertThat(products.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("활성 상태 검색")
    void searchByStatus() {
        ProductSearchCondition condition = ProductSearchCondition.builder()
            .status(Status.INACTIVE)
            .build();
        List<Product> products = productRepository.findByConditionForAdmin(condition);
        assertThat(products.size()).isEqualTo(0);
    }
}