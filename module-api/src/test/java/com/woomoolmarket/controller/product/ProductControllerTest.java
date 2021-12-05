package com.woomoolmarket.controller.product;

import static com.woomoolmarket.helper.ProductTestHelper.PRODUCT_DESCRIPTION;
import static com.woomoolmarket.helper.ProductTestHelper.PRODUCT_NAME;
import static com.woomoolmarket.helper.ProductTestHelper.PRODUCT_PRICE;
import static com.woomoolmarket.helper.ProductTestHelper.PRODUCT_STOCK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woomoolmarket.common.enumeration.Region;
import com.woomoolmarket.config.ApiControllerConfig;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.entity.ProductCategory;
import com.woomoolmarket.service.product.dto.request.ProductModifyRequest;
import com.woomoolmarket.service.product.dto.request.ProductRequest;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser(username = "panda@naver.com", roles = "SELLER")
class ProductControllerTest extends ApiControllerConfig {

    @BeforeEach
    void init() {
        Member member = memberTestHelper.createUser();
        MEMBER_ID = member.getId();

        Product product = productTestHelper.createProduct(member);
        PRODUCT_ID = product.getId();

        Objects.requireNonNull(stringRedisTemplate.keys("*")).forEach(k -> stringRedisTemplate.delete(k));
    }

    @Test
    @DisplayName("상품 단건 조회 성공")
    void getById() throws Exception {
        mockMvc.perform(
                get("/api/products/" + PRODUCT_ID))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("name").value(PRODUCT_NAME))
            .andExpect(jsonPath("description").value(PRODUCT_DESCRIPTION))
            .andExpect(jsonPath("memberResponse").exists())
            .andExpect(jsonPath("price").value(PRODUCT_PRICE))
            .andExpect(jsonPath("stock").value(PRODUCT_STOCK))
            .andExpect(jsonPath("productCategory").value("MEAT"))
            .andExpect(jsonPath("region").value("GANGWONDO"))
            .andExpect(jsonPath("createdDateTime").exists())
            .andExpect(jsonPath("_links").exists());
    }

    @Test
    @DisplayName("상품 단건 조회 실패 - 404 존재하지 않는 상품")
    void getByIdFail() throws Exception {
        mockMvc.perform(
                get("/api/products/" + 0))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("상품 목록 조회 성공")
    void getListBySearchConditionForMember() throws Exception {
        mockMvc.perform(
                get("/api/products"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("_embedded.productResponseList[0].name").value(PRODUCT_NAME))
            .andExpect(jsonPath("_links").exists())
            .andExpect(jsonPath("page").exists());
    }

    @Test
    @DisplayName("상품 목록 조회 실패 - 405 지원하지 않는 메서드")
    void getListBySearchConditionForMemberFail() throws Exception {
        mockMvc.perform(
                patch("/api/products"))
            .andDo(print())
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @DisplayName("상품 추가 성공")
    @WithMockUser(roles = "ADMIN")
    void create() throws Exception {
        ProductRequest productRequest = ProductRequest.builder()
            .name("panda")
            .email("panda@naver.com")
            .productImage("yahoo")
            .productCategory(ProductCategory.CEREAL)
            .price(5000)
            .stock(50000)
            .region(Region.JEJUDO)
            .description("panda is cool")
            .build();

        mockMvc.perform(
                post("/api/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productRequest)))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("상품 추가 실패 - 403 권한 없음")
    @WithMockUser(roles = "USER")
    void createFail() throws Exception {
        ProductRequest productRequest = ProductRequest.builder()
            .email("panda@naver.com")
            .productImage("yahoo")
            .productCategory(ProductCategory.CEREAL)
            .price(5000)
            .stock(50000)
            .region(Region.JEJUDO)
            .description("panda is cool")
            .build();

        mockMvc.perform(
                post("/api/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productRequest)))
            .andDo(print())
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("상품 정보 수정 성공")
    void editProduct() throws Exception {
        ProductModifyRequest modifyRequest = ProductModifyRequest.builder()
            .name("brown bear")
            .build();

        mockMvc.perform(
                patch("/api/products/" + PRODUCT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(modifyRequest)))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("상품 정보 수정 실패 - 400 @Valid 작동")
    void editProductFail() throws Exception {
        ProductModifyRequest modifyRequest = ProductModifyRequest.builder()
            .name("b")
            .build();

        mockMvc.perform(
                patch("/api/products/" + PRODUCT_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(modifyRequest)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("상품 삭제 성공")
    void deleteProduct() throws Exception {
        mockMvc.perform(
                delete("/api/products/" + PRODUCT_ID))
            .andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("상품 삭제 실패 - 403 권한 없음")
    @WithMockUser(roles = "USER")
    void deleteProductFail() throws Exception {
        mockMvc.perform(
                delete("/api/products/" + PRODUCT_ID))
            .andDo(print())
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("관리자 상품 목록 조회 성공")
    @WithMockUser(roles = "ADMIN")
    void getListBySearchConditionForAdmin() throws Exception {
        mockMvc.perform(
                get("/api/products/admin"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("_embedded.productResponseList[0].name").value(PRODUCT_NAME))
            .andExpect(jsonPath("_embedded.productResponseList[0].description").value(PRODUCT_DESCRIPTION))
            .andExpect(jsonPath("_embedded.productResponseList[0].price").value(PRODUCT_PRICE))
            .andExpect(jsonPath("_links").exists())
            .andExpect(jsonPath("page").exists());
    }

    @Test
    @DisplayName("관리자 상품 목록 조회 실패 - 403 권한 없음")
    void getListBySearchConditionForAdminFail() throws Exception {
        mockMvc.perform(
                get("/api/products/admin"))
            .andDo(print())
            .andExpect(status().isForbidden());
    }
}