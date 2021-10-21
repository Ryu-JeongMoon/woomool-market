package com.woomoolmarket.controller.cart;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.woomoolmarket.common.enumeration.Region;
import com.woomoolmarket.config.ApiDocumentationConfig;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.purchase.cart.entity.Cart;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.entity.ProductCategory;
import com.woomoolmarket.service.cart.dto.request.CartRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

class CartControllerDocumentation extends ApiDocumentationConfig {

    private static final String USERNAME = "panda@naver.com";
    private static final String PASSWORD = "123456";
    private static final String NICKNAME = "panda";
    private static Long MEMBER_ID;
    private static Long PRODUCT_ID;
    private static Long CART_ID;

    @BeforeEach
    void init() {
        em.createNativeQuery("ALTER TABLE CART ALTER COLUMN `cart_id` RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE MEMBER ALTER COLUMN `member_id` RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE PRODUCT ALTER COLUMN `product_id` RESTART WITH 1").executeUpdate();

        Member member = Member.builder()
            .email(USERNAME)
            .password(PASSWORD)
            .nickname(NICKNAME)
            .build();
        MEMBER_ID = memberRepository.save(member).getId();

//        em.flush();

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

//        em.flush();


        Cart cart = Cart.builder()
            .member(member)
            .product(product)
            .quantity(500)
            .build();
        CART_ID = cartRepository.save(cart).getId();
    }

    @Test
    @DisplayName("장바구니 조회")
    @WithMockUser(username = USERNAME, roles = "USER")
    void getListByMember() throws Exception {
        mockMvc.perform(
                get("/api/carts/" + MEMBER_ID)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.ALL))
            .andDo(document("cart/get-carts",
                relaxedResponseFields(
                    fieldWithPath("_embedded.cartResponseList[].id").type(JsonFieldType.NUMBER).description("장바구니 고유 번호"),
                    fieldWithPath("_embedded.cartResponseList[].quantity").type(JsonFieldType.NUMBER).description("주문 수량"),
                    subsectionWithPath("_embedded.cartResponseList[].memberResponse").type(JsonFieldType.OBJECT)
                        .description("주문 회원"),
                    subsectionWithPath("_embedded.cartResponseList[].productResponse").type(JsonFieldType.OBJECT)
                        .description("주문 상품")
                )));
    }

    @Test
    @DisplayName("장바구니 추가")
    @WithMockUser(username = USERNAME, roles = "USER")
    void addToCart() throws Exception {
        CartRequest cartRequest = CartRequest.builder()
            .memberId(MEMBER_ID)
            .productId(PRODUCT_ID)
            .quantity(500).build();

        mockMvc.perform(
                post("/api/carts/" + MEMBER_ID)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.ALL)
                    .content(objectMapper.writeValueAsString(cartRequest)))
            .andDo(document("cart/add-cart",
                requestFields(
                    fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("주문 회원 고유 번호")
                        .attributes(key("constraint").value("Not Null")),
                    fieldWithPath("productId").type(JsonFieldType.NUMBER).description("상품 고유 번호")
                        .attributes(key("constraint").value("Not Null")),
                    fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("주문 수량")
                        .attributes(key("constraint").value("Not Null"))
                )));
    }

    void removeAll() {

    }

    void getOneById() {

    }

    void removeOne() {

    }
}