package com.woomoolmarket.controller.cart;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.common.enumeration.Region;
import com.woomoolmarket.domain.member.entity.Address;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.domain.purchase.cart.entity.Cart;
import com.woomoolmarket.domain.purchase.cart.repository.CartRepository;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.entity.ProductCategory;
import com.woomoolmarket.domain.purchase.product.repository.ProductRepository;
import com.woomoolmarket.service.cart.dto.request.CartRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@WithMockUser(username = "panda@naver.com", roles = "USER")
class CartControllerTest {

    private static final String MEMBER_EMAIL = "panda@naver.com";
    private static final String MEMBER_PASSWORD = "123456";
    private static final Integer CART_QUANTITY = 500;
    private static Long CART_ID;
    private static Long PRODUCT_ID;
    private static Long MEMBER_ID;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void init() {
        Member member = Member.builder()
            .email(MEMBER_EMAIL)
            .password(MEMBER_PASSWORD)
            .address(new Address("seoul", "yeonhui", "padaro"))
            .build();
        MEMBER_ID = memberRepository.save(member).getId();

        Product product = Product.builder()
            .member(member)
            .name("panda")
            .price(50000)
            .stock(10000)
            .region(Region.GANGWONDO)
            .productCategory(ProductCategory.MEAT)
            .description("panda is bear")
            .build();
        PRODUCT_ID = productRepository.save(product).getId();

        Cart cart = Cart.builder()
            .member(member)
            .product(product)
            .quantity(CART_QUANTITY)
            .build();
        CART_ID = cartRepository.save(cart).getId();
    }

    @Test
    @DisplayName("장바구니 단건 조회 성공")
    void getOneById() throws Exception {
        mockMvc.perform(
                get("/api/carts/" + MEMBER_ID + "/" + CART_ID))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").value(CART_ID))
            .andExpect(jsonPath("quantity").value(CART_QUANTITY))
            .andExpect(jsonPath("memberResponse").exists())
            .andExpect(jsonPath("productResponse").exists());
    }

    @Test
    @DisplayName("장바구니 단건 조회 실패 - 400 잘못된 요청 (GET -> POST)")
    void getOneByIdFail() throws Exception {
        mockMvc.perform(
                post("/api/carts/" + MEMBER_ID + "/" + CART_ID))
            .andDo(print())
            .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @DisplayName("장바구니 다건 조회 성공")
    void getListByMember() throws Exception {
        mockMvc.perform(
                get("/api/carts/" + MEMBER_ID))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("_embedded.cartResponseList[0].id").value(CART_ID))
            .andExpect(jsonPath("_embedded.cartResponseList[0].quantity").value(CART_QUANTITY))
            .andExpect(jsonPath("_embedded.cartResponseList[0].memberResponse").exists())
            .andExpect(jsonPath("_embedded.cartResponseList[0].productResponse").exists());
    }

    @Test
    @DisplayName("장바구니 다건 조회 실패 - 404 존재하지 않는 회원")
    void getListByMemberFail() throws Exception {
        mockMvc.perform(
                get("/api/carts/" + 2L))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("장바구니 추가 성공")
    void addToCart() throws Exception {
        CartRequest cartRequest = CartRequest.builder()
            .memberId(MEMBER_ID)
            .productId(PRODUCT_ID)
            .quantity(CART_QUANTITY)
            .build();

        mockMvc.perform(
                post("/api/carts/" + MEMBER_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(cartRequest)))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("장바구니 추가 실패 - 400 @Valid 작동")
    void addToCartFail() throws Exception {
        CartRequest cartRequest = CartRequest.builder()
            .memberId(MEMBER_ID)
            .productId(PRODUCT_ID)
            .quantity(0)
            .build();

        mockMvc.perform(
                post("/api/carts/" + MEMBER_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(cartRequest)))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("장바구니 단건 삭제 성공")
    void removeOne() throws Exception {
        mockMvc.perform(
                delete("/api/carts/" + MEMBER_ID))
            .andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("장바구니 단건 삭제 실패 - 404 존재하지 않는 회원")
    void removeOneFail() throws Exception {
        mockMvc.perform(
                delete("/api/carts/" + 0))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("장바구니 비움 성공")
    void removeAll() throws Exception {
        mockMvc.perform(
                delete("/api/carts/" + MEMBER_ID + "/" + CART_ID))
            .andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("장바구니 비움 실패 - 404 존재하지 않는 장바구니")
    void removeAllFail() throws Exception {
        mockMvc.perform(
                delete("/api/carts/" + MEMBER_ID + "/" + 0))
            .andDo(print())
            .andExpect(status().isNotFound());
    }
}