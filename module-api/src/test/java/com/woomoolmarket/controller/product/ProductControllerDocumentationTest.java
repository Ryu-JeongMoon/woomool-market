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

import com.woomoolmarket.domain.entity.enumeration.Region;
import com.woomoolmarket.config.ApiDocumentationConfig;
import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.entity.Product;
import com.woomoolmarket.domain.entity.enumeration.ProductCategory;
import com.woomoolmarket.service.product.dto.request.ProductModifyRequest;
import com.woomoolmarket.service.product.dto.request.ProductRequest;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

@WithMockUser(username = "bear@gmail.com", roles = "SELLER")
class ProductControllerDocumentationTest extends ApiDocumentationConfig {

  @BeforeEach
  void init() {
    Member member = memberTestHelper.createSeller();
    MEMBER_ID = member.getId();

    Product product = productTestHelper.createProduct(member);
    PRODUCT_ID = product.getId();

    Objects.requireNonNull(stringRedisTemplate.keys("*")).forEach(k -> stringRedisTemplate.delete(k));
  }

  @Test
  @DisplayName("?????? ?????? ??????")
  void getListBySearchConditionForMember() throws Exception {
    mockMvc.perform(
        get("/api/products")
          .accept(MediaType.ALL))
      .andDo(document("product/get-products",
        relaxedResponseFields(
          fieldWithPath("_embedded.productQueryResponseList[0].name").type(JsonFieldType.STRING).description("?????? ??????"),
          subsectionWithPath("_embedded.productQueryResponseList[0].memberQueryResponse").type(JsonFieldType.OBJECT)
            .description("?????????"),
          fieldWithPath("_embedded.productQueryResponseList[0].description").type(JsonFieldType.STRING).description("??????"),
          fieldWithPath("_embedded.productQueryResponseList[0].productImage").type(JsonFieldType.STRING).description("??????")
            .optional(),
          fieldWithPath("_embedded.productQueryResponseList[0].price").type(JsonFieldType.NUMBER).description("??????"),
          fieldWithPath("_embedded.productQueryResponseList[0].stock").type(JsonFieldType.NUMBER).description("??????"),
          fieldWithPath("_embedded.productQueryResponseList[0].createdDateTime").type(JsonFieldType.VARIES)
            .description("?????? ?????????"),
          fieldWithPath("_embedded.productQueryResponseList[0].productCategory").type(JsonFieldType.STRING)
            .description("?????? ??????"),
          fieldWithPath("_embedded.productQueryResponseList[0].region").type(JsonFieldType.STRING).description("??????")
        )));
  }

  @Test
  @DisplayName("?????? ??????")
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
          fieldWithPath("name").type(JsonFieldType.STRING).description("?????? ??????")
            .attributes(key(CONSTRAINT).value("?????? ?????? 6-24???")),
          fieldWithPath("email").type(JsonFieldType.STRING).description("?????????")
            .attributes(key(CONSTRAINT).value("????????? ?????? 9-64???")),
          fieldWithPath("description").type(JsonFieldType.STRING).description("?????? ??????")
            .attributes(key(CONSTRAINT).value("?????? ?????? ??? ??? ?????? ??????")),
          fieldWithPath("productImage").type(JsonFieldType.STRING).description("?????? ?????????").optional()
            .attributes(key(CONSTRAINT).value("?????? ?????? ?????? 255???")),
          fieldWithPath("price").type(JsonFieldType.NUMBER).description("??????")
            .attributes(key(CONSTRAINT).value("?????? ??????")),
          fieldWithPath("stock").type(JsonFieldType.NUMBER).description("??????")
            .attributes(key(CONSTRAINT).value("?????? ??????")),
          fieldWithPath("region").type(JsonFieldType.STRING).description("??????")
            .attributes(key(CONSTRAINT).value("Region class ??????")),
          fieldWithPath("productCategory").type(JsonFieldType.STRING).description("?????????")
            .attributes(key(CONSTRAINT).value("ProductCategory class ??????"))
        )));
  }

  @Test
  @DisplayName("?????? ?????? ??????")
  void getById() throws Exception {
    mockMvc.perform(
        get("/api/products/" + PRODUCT_ID)
          .accept(MediaType.ALL))
      .andDo(document("product/get-product",
        relaxedResponseFields(
          fieldWithPath("name").type(JsonFieldType.STRING).description("?????? ??????"),
          fieldWithPath("description").type(JsonFieldType.STRING).description("??????"),
          fieldWithPath("productImage").type(JsonFieldType.STRING).description("??????").optional(),
          fieldWithPath("price").type(JsonFieldType.NUMBER).description("??????"),
          fieldWithPath("stock").type(JsonFieldType.NUMBER).description("??????"),
          fieldWithPath("createdDateTime").type(JsonFieldType.STRING).description("?????????"),
          fieldWithPath("productCategory").type(JsonFieldType.STRING).description("??????"),
          fieldWithPath("region").type(JsonFieldType.STRING).description("??????"),
          subsectionWithPath("memberResponse").type(JsonFieldType.OBJECT).description("?????????")
        )));
  }

  @Test
  @DisplayName("?????? ??????")
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
          fieldWithPath("name").type(JsonFieldType.STRING).description("?????? ??????").optional()
            .attributes(key(CONSTRAINT).value("?????? ?????? 6-24???")),
          fieldWithPath("description").type(JsonFieldType.STRING).description("?????? ??????").optional()
            .attributes(key(CONSTRAINT).value("?????? ?????? ??? ??? ?????? ??????")),
          fieldWithPath("productImage").type(JsonFieldType.STRING).description("?????? ?????????").optional()
            .attributes(key(CONSTRAINT).value("?????? ?????? ?????? 255???")),
          fieldWithPath("price").type(JsonFieldType.NUMBER).description("??????").optional()
            .attributes(key(CONSTRAINT).value("?????? ??????")),
          fieldWithPath("stock").type(JsonFieldType.NUMBER).description("??????").optional()
            .attributes(key(CONSTRAINT).value("?????? ??????")),
          fieldWithPath("region").type(JsonFieldType.STRING).description("??????").optional()
            .attributes(key(CONSTRAINT).value("Region class ??????")),
          fieldWithPath("productCategory").type(JsonFieldType.STRING).description("?????????").optional()
            .attributes(key(CONSTRAINT).value("ProductCategory class ??????"))
        )));
  }

  @Test
  @DisplayName("?????? ?????? - ????????? ??????")
  void deleteProductBySeller() throws Exception {
    mockMvc.perform(
        delete("/api/products/" + PRODUCT_ID))
      .andExpect(status().isNoContent())
      .andDo(document("product/delete-product"));
  }

  @Test
  @DisplayName("?????? ?????? - ?????????")
  @WithMockUser(roles = "ADMIN")
  void deleteProductByAdmin() throws Exception {
    mockMvc.perform(
        delete("/api/products/" + PRODUCT_ID))
      .andExpect(status().isNoContent())
      .andDo(document("product/admin-delete-product"));
  }

  @Test
  @DisplayName("????????? ?????? ?????? ??????")
  @WithMockUser(roles = "ADMIN")
  void getListBySearchConditionForAdmin() throws Exception {
    mockMvc.perform(
        get("/api/products/admin")
          .contentType(MediaType.APPLICATION_JSON_VALUE))
      .andDo(document("product/admin-get-products",
        relaxedResponseFields(
          fieldWithPath("_embedded.productQueryResponseList[0].name").type(JsonFieldType.STRING).description("?????? ??????"),
          fieldWithPath("_embedded.productQueryResponseList[0].description").type(JsonFieldType.STRING).description("?????? ??????"),
          fieldWithPath("_embedded.productQueryResponseList[0].price").type(JsonFieldType.NUMBER).description("??????"),
          fieldWithPath("_embedded.productQueryResponseList[0].stock").type(JsonFieldType.NUMBER).description("??????"),
          fieldWithPath("_embedded.productQueryResponseList[0].region").type(JsonFieldType.STRING).description("??????"),
          fieldWithPath("_embedded.productQueryResponseList[0].productCategory").type(JsonFieldType.STRING)
            .description("?????????"),
          fieldWithPath("_embedded.productQueryResponseList[0].productImage").type(JsonFieldType.STRING).description("?????? ?????????")
            .optional(),
          subsectionWithPath("_embedded.productQueryResponseList[0].memberQueryResponse").type(JsonFieldType.OBJECT)
            .description("?????????")
        )));
  }

  @Test
  @DisplayName("????????? ?????? ?????? ??????")
  @WithMockUser(roles = "ADMIN")
  void getListBySearchConditionForAdminFail() throws Exception {
    mockMvc.perform(
        get("/api/products/admin")
          .contentType(MediaType.APPLICATION_JSON_VALUE))
      .andDo(document("product/admin-get-products",
        relaxedResponseFields(
          fieldWithPath("_embedded.productQueryResponseList[0].name").type(JsonFieldType.STRING).description("?????? ??????"),
          fieldWithPath("_embedded.productQueryResponseList[0].description").type(JsonFieldType.STRING).description("?????? ??????"),
          fieldWithPath("_embedded.productQueryResponseList[0].price").type(JsonFieldType.NUMBER).description("??????"),
          fieldWithPath("_embedded.productQueryResponseList[0].stock").type(JsonFieldType.NUMBER).description("??????"),
          fieldWithPath("_embedded.productQueryResponseList[0].region").type(JsonFieldType.STRING).description("??????"),
          fieldWithPath("_embedded.productQueryResponseList[0].productCategory").type(JsonFieldType.STRING)
            .description("?????????"),
          fieldWithPath("_embedded.productQueryResponseList[0].productImage").type(JsonFieldType.STRING).description("?????? ?????????")
            .optional(),
          subsectionWithPath("_embedded.productQueryResponseList[0].memberQueryResponse").type(JsonFieldType.OBJECT)
            .description("?????????")
        )));
  }
}
