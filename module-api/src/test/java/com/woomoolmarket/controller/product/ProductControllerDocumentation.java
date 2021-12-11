package com.woomoolmarket.controller.product;

import static com.woomoolmarket.helper.MemberTestHelper.MEMBER_EMAIL;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser(username = "panda@naver.com", roles = "SELLER")
class ProductControllerDocumentation extends ApiDocumentationConfig {

    @BeforeEach
    void init() {
        Member member = memberTestHelper.createSeller();
        MEMBER_ID = member.getId();

        Product product = productTestHelper.createProduct(member);
        PRODUCT_ID = product.getId();

        Objects.requireNonNull(stringRedisTemplate.keys("*")).forEach(k -> stringRedisTemplate.delete(k));
    }

    @Test
    @DisplayName("상품 목록 조회")
    void getListBySearchConditionForMember() throws Exception {
        mockMvc.perform(
                get("/api/products")
                    .accept(MediaType.ALL))
            .andDo(document("product/get-products",
                relaxedResponseFields(
                    fieldWithPath("_embedded.productQueryResponseList[0].name").type(JsonFieldType.STRING).description("상품 이름"),
                    subsectionWithPath("_embedded.productQueryResponseList[0].memberQueryResponse").type(JsonFieldType.OBJECT)
                        .description("판매자"),
                    fieldWithPath("_embedded.productQueryResponseList[0].description").type(JsonFieldType.STRING).description("설명"),
                    fieldWithPath("_embedded.productQueryResponseList[0].productImage").type(JsonFieldType.STRING).description("사진")
                        .optional(),
                    fieldWithPath("_embedded.productQueryResponseList[0].price").type(JsonFieldType.NUMBER).description("가격"),
                    fieldWithPath("_embedded.productQueryResponseList[0].stock").type(JsonFieldType.NUMBER).description("재고"),
                    fieldWithPath("_embedded.productQueryResponseList[0].createdDateTime").type(JsonFieldType.VARIES)
                        .description("상품 등록일"),
                    fieldWithPath("_embedded.productQueryResponseList[0].productCategory").type(JsonFieldType.STRING)
                        .description("상품 분류"),
                    fieldWithPath("_embedded.productQueryResponseList[0].region").type(JsonFieldType.STRING).description("지역")
                )));
    }

    @Test
    @DisplayName("상품 추가")
    void create() throws Exception {
        ProductRequest productRequest = ProductRequest.builder()
            .name("apple")
            .description("delicious")
            .productImage("yaho")
            .price(3000)
            .stock(50000)
            .region(Region.CHUNGCHEONGBUKDO)
            .productCategory(ProductCategory.FRUIT)
            .email(MEMBER_EMAIL)
            .build();

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
    void deleteProduct() throws Exception {
        mockMvc.perform(
                delete("/api/products/" + PRODUCT_ID))
            .andExpect(status().isNoContent())
            .andDo(document("product/delete-product"));
    }

    @Test
    @DisplayName("관리자 상품 목록 조회")
    @WithMockUser(roles = "ADMIN")
    void getListBySearchConditionForAdmin() throws Exception {
        mockMvc.perform(
                get("/api/products/admin")
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(document("product/admin-get-products",
                relaxedResponseFields(
                    fieldWithPath("_embedded.productQueryResponseList[0].name").type(JsonFieldType.STRING).description("상풍 이름"),
                    fieldWithPath("_embedded.productQueryResponseList[0].description").type(JsonFieldType.STRING).description("상품 설명"),
                    fieldWithPath("_embedded.productQueryResponseList[0].price").type(JsonFieldType.NUMBER).description("가격"),
                    fieldWithPath("_embedded.productQueryResponseList[0].stock").type(JsonFieldType.NUMBER).description("재고"),
                    fieldWithPath("_embedded.productQueryResponseList[0].region").type(JsonFieldType.STRING).description("지역"),
                    fieldWithPath("_embedded.productQueryResponseList[0].productCategory").type(JsonFieldType.STRING)
                        .description("판매자"),
                    fieldWithPath("_embedded.productQueryResponseList[0].productImage").type(JsonFieldType.STRING).description("상품 이미지")
                        .optional(),
                    subsectionWithPath("_embedded.productQueryResponseList[0].memberQueryResponse").type(JsonFieldType.OBJECT)
                        .description("판매자")
                )));
    }
}
