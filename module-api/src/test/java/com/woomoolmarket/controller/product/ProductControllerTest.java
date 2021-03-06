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

import com.woomoolmarket.domain.entity.enumeration.Region;
import com.woomoolmarket.config.ApiControllerConfig;
import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.entity.Product;
import com.woomoolmarket.domain.entity.enumeration.ProductCategory;
import com.woomoolmarket.service.product.dto.request.ProductModifyRequest;
import com.woomoolmarket.service.product.dto.request.ProductRequest;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.multipart.MultipartFile;

@WithMockUser(username = "panda@naver.com", roles = "SELLER")
class ProductControllerTest extends ApiControllerConfig {

  @BeforeEach
  void init() {
    Member member = memberTestHelper.createMember();
    MEMBER_ID = member.getId();

    Product product = productTestHelper.createProduct(member);
    PRODUCT_ID = product.getId();

    Objects.requireNonNull(stringRedisTemplate.keys("*")).forEach(k -> stringRedisTemplate.delete(k));
  }

  @Test
  @DisplayName("?????? ?????? ?????? ??????")
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
  @DisplayName("?????? ?????? ?????? ?????? - 404 ???????????? ?????? ??????")
  void getByIdFail() throws Exception {
    mockMvc.perform(
        get("/api/products/" + 0))
      .andDo(print())
      .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("?????? ?????? ?????? ??????")
  void getListBySearchConditionForMember() throws Exception {
    mockMvc.perform(
        get("/api/products"))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("_embedded.productQueryResponseList[0].name").value(PRODUCT_NAME))
      .andExpect(jsonPath("_links").exists())
      .andExpect(jsonPath("page").exists());
  }

  @Test
  @DisplayName("?????? ?????? ?????? ?????? - 405 ???????????? ?????? ?????????")
  void getListBySearchConditionForMemberFail() throws Exception {
    mockMvc.perform(
        patch("/api/products"))
      .andDo(print())
      .andExpect(status().isMethodNotAllowed());
  }

  @Test
  @DisplayName("?????? ?????? ??????")
  void create() throws Exception {
    ProductRequest productRequest = ProductRequest.builder()
      .name("panda-bear")
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
  @DisplayName("?????? ????????? ?????? ??????")
  void createWithMultipartFile() throws Exception {
    MultipartFile multipartFile = multipartFileTestHelper.createMultipartFile();

    ProductRequest productRequest = ProductRequest.builder()
      .name("panda-bear")
      .email("panda@naver.com")
      .productImage("yahoo")
      .productCategory(ProductCategory.CEREAL)
      .price(5000)
      .stock(50000)
      .multipartFiles(List.of(multipartFile))
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
  @DisplayName("?????? ?????? ?????? - 403 ?????? ??????")
  @WithMockUser(roles = "USER")
  void createFail() throws Exception {
    ProductRequest productRequest = ProductRequest.builder()
      .email("panda@naver.com")
      .name("PANDA")
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
  @DisplayName("?????? ?????? ?????? ??????")
  void editProduct() throws Exception {
    ProductModifyRequest modifyRequest = ProductModifyRequest.builder()
      .name("brown bear")
      .description("bear bear")
      .build();

    mockMvc.perform(
        patch("/api/products/" + PRODUCT_ID)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(modifyRequest)))
      .andDo(print())
      .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("?????? ????????? ?????? ??????")
  void editWithMultipartFileProduct() throws Exception {
    MultipartFile multipartFile = multipartFileTestHelper.createMultipartFile();

    ProductModifyRequest modifyRequest = ProductModifyRequest.builder()
      .name("brown bear")
      .description("bear bear")
      .multipartFiles(List.of(multipartFile))
      .build();

    mockMvc.perform(
        patch("/api/products/" + PRODUCT_ID)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(modifyRequest)))
      .andDo(print())
      .andExpect(status().isCreated());
  }

  @Test
  @DisplayName("?????? ?????? ?????? ?????? - 400 @Valid ??????")
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
  @DisplayName("?????? ?????? ??????")
  void deleteProduct() throws Exception {
    mockMvc.perform(
        delete("/api/products/" + PRODUCT_ID))
      .andDo(print())
      .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("?????? ?????? ?????? - ?????????")
  @WithMockUser(roles = "ADMIN")
  void deleteProductByAdmin() throws Exception {
    mockMvc.perform(
        delete("/api/products/" + PRODUCT_ID))
      .andDo(print())
      .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("?????? ?????? ?????? - 403 ?????? ??????")
  @WithMockUser(roles = "USER")
  void deleteProductFail() throws Exception {
    mockMvc.perform(
        delete("/api/products/" + PRODUCT_ID))
      .andDo(print())
      .andExpect(status().isForbidden());
  }

  @Test
  @DisplayName("????????? ?????? ?????? ?????? ??????")
  @WithMockUser(roles = "ADMIN")
  void getListBySearchConditionForAdmin() throws Exception {
    mockMvc.perform(
        get("/api/products/admin"))
      .andDo(print())
      .andExpect(status().isOk())
      .andExpect(jsonPath("_embedded.productQueryResponseList[0].name").value(PRODUCT_NAME))
      .andExpect(jsonPath("_embedded.productQueryResponseList[0].description").value(PRODUCT_DESCRIPTION))
      .andExpect(jsonPath("_embedded.productQueryResponseList[0].price").value(PRODUCT_PRICE))
      .andExpect(jsonPath("_links").exists())
      .andExpect(jsonPath("page").exists());
  }

  @Test
  @DisplayName("????????? ?????? ?????? ?????? ?????? - 403 ?????? ??????")
  void getListBySearchConditionForAdminFail() throws Exception {
    mockMvc.perform(
        get("/api/products/admin"))
      .andDo(print())
      .andExpect(status().isForbidden());
  }
}