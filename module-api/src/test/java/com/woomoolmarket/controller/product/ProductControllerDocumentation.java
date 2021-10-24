package com.woomoolmarket.controller.product;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woomoolmarket.common.enumeration.Region;
import com.woomoolmarket.config.ApiDocumentationConfig;
import com.woomoolmarket.domain.member.entity.Address;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.entity.ProductCategory;
import com.woomoolmarket.service.product.dto.request.ProductModifyRequest;
import com.woomoolmarket.service.product.dto.request.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

class ProductControllerDocumentation extends ApiDocumentationConfig {

    private static final String USERNAME = "panda@naver.com";
    private static final String PASSWORD = "123456";
    private static final String NICKNAME = "panda";
    private static Long MEMBER_ID;
    private static Long PRODUCT_ID;

    @BeforeEach
    void initialize() throws Exception {
        em.createNativeQuery("ALTER TABLE PRODUCT ALTER COLUMN `product_id` RESTART WITH 1").executeUpdate();

        Member member = Member.builder()
            .email(USERNAME)
            .password(PASSWORD)
            .nickname(NICKNAME)
            .address(new Address("seoul", "yeonhui", "01023"))
            .phone("01012345678")
            .build();
        MEMBER_ID = memberRepository.save(member).getId();

        Product product = Product.builder()
            .member(member)
            .name("panda")
            .price(50000)
            .description("nice bear")
            .productCategory(ProductCategory.MEAT)
            .stock(15000)
            .region(Region.CHUNGCHEONGBUKDO)
            .build();
        PRODUCT_ID = productRepository.save(product).getId();
    }

    @Test
    @DisplayName("상품 목록 조회")
    @WithMockUser(username = USERNAME, roles = "USER")
    void getListBySearchConditionForMember() throws Exception {
        mockMvc.perform(
                get("/api/products")
                    .accept(MediaType.ALL))
            .andDo(document("product/get-products",
                relaxedResponseFields(
                    fieldWithPath("_embedded.productResponseList[].name").type(JsonFieldType.STRING).description("상품 이름"),
                    subsectionWithPath("_embedded.productResponseList[].memberResponse").type(JsonFieldType.OBJECT)
                        .description("판매자"),
                    fieldWithPath("_embedded.productResponseList[].description").type(JsonFieldType.STRING).description("설명"),
                    fieldWithPath("_embedded.productResponseList[].productImage").type(JsonFieldType.STRING).description("사진")
                        .optional(),
                    fieldWithPath("_embedded.productResponseList[].price").type(JsonFieldType.NUMBER).description("가격"),
                    fieldWithPath("_embedded.productResponseList[].stock").type(JsonFieldType.NUMBER).description("재고"),
                    fieldWithPath("_embedded.productResponseList[].createdDateTime").type(JsonFieldType.VARIES)
                        .description("상품 등록일"),
                    fieldWithPath("_embedded.productResponseList[].productCategory").type(JsonFieldType.STRING)
                        .description("상품 분류"),
                    fieldWithPath("_embedded.productResponseList[].region").type(JsonFieldType.STRING).description("지역")
                )));
    }

    @Test
    @DisplayName("상품 추가")
    @WithMockUser(username = USERNAME, roles = "SELLER")
    void create() throws Exception {
        ProductRequest productRequest = ProductRequest.builder()
            .name("apple")
            .description("delicious")
            .productImage("yaho")
            .price(3000)
            .stock(50000)
            .region(Region.CHUNGCHEONGBUKDO)
            .productCategory(ProductCategory.FRUIT)
            .email(USERNAME).build();

        mockMvc.perform(
                post("/api/products")
                    .content(objectMapper.writeValueAsString(productRequest))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(document("product/create-product",
                relaxedRequestFields(
                    fieldWithPath("name").type(JsonFieldType.STRING).description("상풍 이름")
                        .attributes(key("constraint").value("문자 형식 6-24자")),
                    fieldWithPath("email").type(JsonFieldType.STRING).description("판매자")
                        .attributes(key("constraint").value("이메일 형식 9-64자")),
                    fieldWithPath("description").type(JsonFieldType.STRING).description("상품 설명")
                        .attributes(key("constraint").value("자유 형식 빈 값 허용 안함")),
                    fieldWithPath("productImage").type(JsonFieldType.STRING).description("상품 이미지").optional()
                        .attributes(key("constraint").value("문자 형식 최대 255자")),
                    fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격")
                        .attributes(key("constraint").value("숫자 형식")),
                    fieldWithPath("stock").type(JsonFieldType.NUMBER).description("재고")
                        .attributes(key("constraint").value("숫자 형식")),
                    fieldWithPath("region").type(JsonFieldType.STRING).description("지역")
                        .attributes(key("constraint").value("Region class 참고")),
                    fieldWithPath("productCategory").type(JsonFieldType.STRING).description("판매자")
                        .attributes(key("constraint").value("ProductCategory class 참고"))
                )));
    }

    @Test
    @DisplayName("상품 단건 조회")
    @WithMockUser(username = USERNAME, roles = "USER")
    void getById() throws Exception {
        mockMvc.perform(
                get("/api/products/" + PRODUCT_ID)
                    .accept(MediaType.ALL))
            .andDo(document("product/get-product",
                relaxedResponseFields(
                    fieldWithPath("name").type(JsonFieldType.STRING).description("상품 이름"),
                    fieldWithPath("description").type(JsonFieldType.STRING).description("설명"),
                    fieldWithPath("productImage").type(JsonFieldType.STRING).description("사진").optional(),
                    fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격"),
                    fieldWithPath("stock").type(JsonFieldType.NUMBER).description("재고"),
                    fieldWithPath("createdDateTime").type(JsonFieldType.STRING).description("등록일"),
                    fieldWithPath("productCategory").type(JsonFieldType.STRING).description("분류"),
                    fieldWithPath("region").type(JsonFieldType.STRING).description("지역"),
                    subsectionWithPath("memberResponse").type(JsonFieldType.OBJECT).description("판매자")
                )));
    }

    @Test
    @DisplayName("상품 수정")
    @WithMockUser(username = USERNAME, roles = "SELLER")
    void editProduct() throws Exception {
        ProductModifyRequest modifyRequest = ProductModifyRequest.builder()
            .name("bear")
            .description("good panda")
            .build();

        mockMvc.perform(
                patch("/api/products/" + PRODUCT_ID)
                    .content(objectMapper.writeValueAsString(modifyRequest))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(document("product/edit-product",
                relaxedRequestFields(
                    fieldWithPath("name").type(JsonFieldType.STRING).description("상풍 이름").optional()
                        .attributes(key("constraint").value("문자 형식 6-24자")),
                    fieldWithPath("description").type(JsonFieldType.STRING).description("상품 설명").optional()
                        .attributes(key("constraint").value("자유 형식 빈 값 허용 안함")),
                    fieldWithPath("productImage").type(JsonFieldType.STRING).description("상품 이미지").optional()
                        .attributes(key("constraint").value("문자 형식 최대 255자")),
                    fieldWithPath("price").type(JsonFieldType.NUMBER).description("가격").optional()
                        .attributes(key("constraint").value("숫자 형식")),
                    fieldWithPath("stock").type(JsonFieldType.NUMBER).description("재고").optional()
                        .attributes(key("constraint").value("숫자 형식")),
                    fieldWithPath("region").type(JsonFieldType.STRING).description("지역").optional()
                        .attributes(key("constraint").value("Region class 참고")),
                    fieldWithPath("productCategory").type(JsonFieldType.STRING).description("판매자").optional()
                        .attributes(key("constraint").value("ProductCategory class 참고"))
                )));
    }

    @Test
    @DisplayName("상품 삭제")
    @WithMockUser(username = USERNAME, roles = "SELLER")
    void deleteProduct() throws Exception {
        mockMvc.perform(
                delete("/api/products/" + PRODUCT_ID))
            .andExpect(status().isNoContent())
            .andDo(document("product/delete-product"));
    }

    @Test
    @DisplayName("관리자 상품 목록 조회")
    @WithMockUser(username = USERNAME, roles = "ADMIN")
    void getListBySearchConditionForAdmin() throws Exception {
        mockMvc.perform(
                get("/api/products/admin")
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(document("product/admin-get-products",
                relaxedResponseFields(
                    fieldWithPath("_embedded.productResponseList[].name").type(JsonFieldType.STRING).description("상풍 이름"),
                    fieldWithPath("_embedded.productResponseList[].description").type(JsonFieldType.STRING).description("상품 설명"),
                    fieldWithPath("_embedded.productResponseList[].price").type(JsonFieldType.NUMBER).description("가격"),
                    fieldWithPath("_embedded.productResponseList[].stock").type(JsonFieldType.NUMBER).description("재고"),
                    fieldWithPath("_embedded.productResponseList[].region").type(JsonFieldType.STRING).description("지역"),
                    fieldWithPath("_embedded.productResponseList[].productCategory").type(JsonFieldType.STRING)
                        .description("판매자"),
                    fieldWithPath("_embedded.productResponseList[].productImage").type(JsonFieldType.STRING).description("상품 이미지")
                        .optional(),
                    subsectionWithPath("_embedded.productResponseList[].memberResponse").type(JsonFieldType.OBJECT)
                        .description("판매자")
                )));
    }
}