package com.woomoolmarket.service.product;

import static org.assertj.core.api.Assertions.assertThat;

import com.woomoolmarket.common.enumeration.Region;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.entity.ProductCategory;
import com.woomoolmarket.domain.purchase.product.repository.ProductRepository;
import com.woomoolmarket.domain.purchase.product.repository.ProductSearchCondition;
import com.woomoolmarket.service.product.dto.request.CreateProductRequest;
import com.woomoolmarket.service.product.dto.request.ModifyProductRequest;
import com.woomoolmarket.service.product.dto.response.ProductResponse;
import com.woomoolmarket.service.product.mapper.ProductResponseMapper;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Transactional
@SpringBootTest
class ProductServiceTest {

    private static final String MEMBER_EMAIL = "panda@naver.com";
    private static Long PRODUCT1_ID;
    private static Long PRODUCT2_ID;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductService productService;
    @Autowired
    ProductResponseMapper productResponseMapper;
    @Autowired
    EntityManager em;

    @BeforeEach
    void init() {
        em.createNativeQuery("ALTER TABLE PRODUCT ALTER COLUMN `product_id` RESTART WITH 1").executeUpdate();

        Member member = Member.builder()
            .email(MEMBER_EMAIL)
            .nickname("bear")
            .password("123456")
            .build();

        memberRepository.save(member);

        Product product1 = Product.builder()
            .name("panda")
            .seller("bear")
            .price(50000)
            .stock(3000)
            .productCategory(ProductCategory.CEREAL)
            .description("panda bear")
            .region(Region.JEJUDO)
            .build();

        Product product2 = Product.builder()
            .name("tiger")
            .seller("cat")
            .price(30000)
            .stock(5000)
            .productCategory(ProductCategory.FISH)
            .description("tiger cat")
            .region(Region.GANGWONDO)
            .build();

        PRODUCT1_ID = productRepository.save(product1).getId();
        PRODUCT2_ID = productRepository.save(product2).getId();
    }

    @Test
    @DisplayName("상품 번호로 조회")
    void getByIdAndStatus() {
        ProductResponse productResponse = productService.getByIdAndStatus(PRODUCT1_ID, Status.ACTIVE);
        assertThat(productResponse.getPrice()).isEqualTo(50000);
    }

    @Test
    @DisplayName("검색 조건 - 이름으로 조회")
    void getListBySearchConditionForMember() {
        ProductSearchCondition condition = ProductSearchCondition.builder()
            .name("an")
            .build();
        List<ProductResponse> productResponses = productService.getListBySearchConditionForMember(condition);
        assertThat(productResponses.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("검색 조건 - 카테고리로 조회")
    void getListBySearchConditionForMember2() {
        ProductSearchCondition condition = ProductSearchCondition.builder()
            .category(ProductCategory.FISH)
            .build();
        List<ProductResponse> productResponses = productService.getListBySearchConditionForMember(condition);
    }

    @Test
    @DisplayName("상품 생성 성공")
    void create() {
        CreateProductRequest createProductRequest = CreateProductRequest.builder()
            .name("dog")
            .price(15000)
            .seller("postman")
            .stock(900)
            .description("mung mung")
            .region(Region.CHUNGCHEONGBUKDO)
            .productCategory(ProductCategory.CEREAL).build();
        productService.create(createProductRequest);

        assertThat(productRepository.findById(3L).get()).isNotNull();
    }

    @Test
    @DisplayName("가격 변경")
    void edit() {
        ModifyProductRequest modifyProductRequest = ModifyProductRequest.builder()
            .price(90000)
            .build();
        ProductResponse productResponse = productService.edit(PRODUCT1_ID, modifyProductRequest);
        assertThat(productResponse.getPrice()).isEqualTo(90000);
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
            .seller("ca")
            .build();

        List<ProductResponse> productResponses = productService.getListBySearchConditionForAdmin(condition);
        assertThat(productResponses.size()).isEqualTo(1);
    }
}