package com.woomoolmarket.controller.member;


import static com.woomoolmarket.helper.MemberTestHelper.MEMBER_EMAIL;
import static com.woomoolmarket.helper.MemberTestHelper.MEMBER_PASSWORD;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woomoolmarket.domain.entity.embeddable.Address;
import com.woomoolmarket.config.ApiDocumentationConfig;
import com.woomoolmarket.service.member.dto.request.LoginRequest;
import com.woomoolmarket.service.member.dto.request.ModifyRequest;
import com.woomoolmarket.service.member.dto.request.SignupRequest;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;

class MemberControllerDocumentationTest extends ApiDocumentationConfig {

  @BeforeEach
  void init() {
    MEMBER_ID = memberTestHelper.createMember().getId();
    Objects.requireNonNull(stringRedisTemplate.keys("*")).forEach(k -> stringRedisTemplate.delete(k));
  }

  @Test
  @DisplayName("????????????")
  @WithMockUser(username = MEMBER_EMAIL, roles = "USER")
  void findMemberTest() throws Exception {
    mockMvc.perform(
        get("/api/members/" + MEMBER_ID)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaTypes.HAL_JSON))
      .andDo(document("member/get-member",
        responseFields(
          fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ??????"),
          fieldWithPath("email").type(JsonFieldType.STRING).description("?????????"),
          fieldWithPath("nickname").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
          fieldWithPath("profileImage").type(JsonFieldType.STRING).description("????????? ??????").optional(),
          fieldWithPath("phone").type(JsonFieldType.STRING).description("?????? ??????").optional(),
          fieldWithPath("license").type(JsonFieldType.STRING).description("????????? ??????").optional(),
          fieldWithPath("createdDateTime").type(JsonFieldType.VARIES).description("?????? ?????????"),
          fieldWithPath("lastModifiedDateTime").type(JsonFieldType.VARIES).description("?????? ?????????"),
          fieldWithPath("leaveDateTime").type(JsonFieldType.VARIES).description("?????????").optional(),
          fieldWithPath("authority").type(JsonFieldType.STRING).description("??????"),
          fieldWithPath("authProvider").type(JsonFieldType.STRING).description("?????? ????????? ??????").optional(),
          fieldWithPath("status").type(JsonFieldType.STRING).description("????????? ??????"),
          subsectionWithPath("address").type(JsonFieldType.OBJECT).description("?????? - ?????????, ?????????, ????????????").optional(),
          subsectionWithPath("_links").type(JsonFieldType.OBJECT).description("HATEOAS")
        )));
  }

  @Test
  @DisplayName("????????????")
  public void signUpSuccessTest() throws Exception {
    SignupRequest signUpRequest = SignupRequest.builder()
      .email("pandabear@gogo.com")
      .nickname("horagin")
      .password("123456")
      .address(new Address("seoul", "yeonhui", "1234"))
      .build();

    mockMvc.perform(
        post("/api/members")
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaTypes.HAL_JSON)
          .content(objectMapper.writeValueAsString(signUpRequest)))
      .andDo(document("member/join-member",
        requestFields(
          fieldWithPath("email").type(JsonFieldType.STRING).description("?????? ???????????? ????????? ?????????")
            .attributes(key(CONSTRAINT).value("????????? ??????, 9-64???")),
          fieldWithPath("nickname").type(JsonFieldType.STRING).description("?????? ?????? or ??????")
            .attributes(key(CONSTRAINT).value("?????? ??????, 4-24???")),
          fieldWithPath("password").type(JsonFieldType.STRING).description("????????????")
            .attributes(key(CONSTRAINT).value("?????? ??????, 4-24???")),
          fieldWithPath("license").type(JsonFieldType.STRING).description("???????????????").optional()
            .attributes(key(CONSTRAINT).value("?????? ?????? 10???")),
          fieldWithPath("address.city").type(JsonFieldType.STRING).description("?????? - ?????????").optional()
            .attributes(key(CONSTRAINT).value("?????? ??????, 2-24???")),
          fieldWithPath("address.street").type(JsonFieldType.STRING).description("?????? - ?????????").optional()
            .attributes(key(CONSTRAINT).value("?????? ??????, 2-24???")),
          fieldWithPath("address.zipcode").type(JsonFieldType.STRING).description("?????? - ????????????").optional()
            .attributes(key(CONSTRAINT).value("?????? ??????, 5-6???")),
          fieldWithPath("multipartFile").type(JsonFieldType.VARIES).description("????????? ??????").optional()
            .attributes(key(CONSTRAINT).value("MultipartFile ??????"))
        )));
  }

  @Test
  @DisplayName("?????? ?????????")
  void loginTest() throws Exception {
    LoginRequest loginRequest = LoginRequest.builder()
      .email(MEMBER_EMAIL)
      .password(MEMBER_PASSWORD)
      .build();

    mockMvc.perform(
        post("/api/auth/login")
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaTypes.HAL_JSON_VALUE)
          .content(objectMapper.writeValueAsString(loginRequest)))
      .andDo(document("member/login-member",
        requestFields(
          fieldWithPath("email").type(JsonFieldType.STRING).description("?????????")
            .attributes(key(CONSTRAINT).value("????????? ?????? 9-64???")),
          fieldWithPath("password").type(JsonFieldType.STRING).description("????????????")
            .attributes(key(CONSTRAINT).value("?????? ?????? 4-24???"))
        )));
  }

  @Test
  @DisplayName("?????? ?????? ??????")
  @WithMockUser(username = MEMBER_EMAIL, roles = "USER")
  void modifyTest() throws Exception {
    ModifyRequest modifyRequest = ModifyRequest.builder()
      .nickname("kcin")
      .password("654321")
      .address(new Address("??????", "?????????", "??????"))
      .build();

    mockMvc.perform(
        patch("/api/members/" + MEMBER_ID)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaTypes.HAL_JSON)
          .content(objectMapper.writeValueAsString(modifyRequest)))
      .andDo(document("member/modify-member",
        requestFields(
          fieldWithPath("nickname").type(JsonFieldType.STRING).description("?????? ?????? ?????? ??????").optional()
            .attributes(key(CONSTRAINT).value("?????? ?????? 6-24???")),
          fieldWithPath("password").type(JsonFieldType.STRING).description("????????????").optional()
            .attributes(key(CONSTRAINT).value("?????? ?????? 4-24???")),
          fieldWithPath("profileImage").type(JsonFieldType.STRING).description("????????? ??????").optional()
            .attributes(key(CONSTRAINT).value("?????? ?????? ?????? 255???")),
          fieldWithPath("phone").type(JsonFieldType.STRING).description("????????????").optional()
            .attributes(key(CONSTRAINT).value("?????? ?????? 10-11???")),
          fieldWithPath("license").type(JsonFieldType.STRING).description("????????? ??????").optional()
            .attributes(key(CONSTRAINT).value("?????? ?????? 10???")),
          fieldWithPath("address.city").type(JsonFieldType.STRING).description("?????? - ?????????").optional()
            .attributes(key(CONSTRAINT).value("?????? ??????, 2-24???")),
          fieldWithPath("address.street").type(JsonFieldType.STRING).description("?????? - ?????????").optional()
            .attributes(key(CONSTRAINT).value("?????? ??????, 2-24???")),
          fieldWithPath("address.zipcode").type(JsonFieldType.STRING).description("?????? - ????????????").optional()
            .attributes(key(CONSTRAINT).value("?????? ??????, 5-6???")),
          fieldWithPath("multipartFile").type(JsonFieldType.VARIES).description("????????? ??????").optional()
            .attributes(key(CONSTRAINT).value("MultipartFile ??????"))
        )));
  }

  @Test
  @DisplayName("?????? ??????")
  @WithMockUser(username = MEMBER_EMAIL, roles = "USER")
  void leaveTest() throws Exception {
    mockMvc.perform(
        delete("/api/members/" + MEMBER_ID)
          .accept(MediaType.APPLICATION_JSON_VALUE))
      .andDo(document("member/leave-member"));
  }

  @Test
  @DisplayName("?????? ?????? ??????")
  @WithMockUser(roles = "ADMIN")
  void restoreTest() throws Exception {
    memberService.leaveSoftly(MEMBER_ID);

    mockMvc.perform(
        get("/api/members/deleted/" + MEMBER_ID)
          .accept(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(status().isCreated())
      .andDo(print())
      .andDo(document("member/restore-member"));
  }

  @Test
  @DisplayName("????????? ?????? ?????? ??????")
  @WithMockUser(roles = "ADMIN")
  void adminFindMemberTest() throws Exception {
    SignupRequest signUpRequest = SignupRequest.builder()
      .email("panda@gmail.com")
      .nickname("nick")
      .password("123456")
      .address(new Address("seoul", "yeonhui", "1234"))
      .build();

    Long memberId = memberService.joinAsMember(signUpRequest).getId();

    mockMvc.perform(
        get("/api/members/admin/" + memberId)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .accept(MediaTypes.HAL_JSON))
      .andDo(print())
      .andDo(document("member/admin-get-member",
        responseFields(
          fieldWithPath("id").type(JsonFieldType.NUMBER).description("?????? ?????? ??????"),
          fieldWithPath("email").type(JsonFieldType.STRING).description("?????????"),
          fieldWithPath("nickname").type(JsonFieldType.STRING).description("?????? ?????? ?????? ??????"),
          fieldWithPath("profileImage").type(JsonFieldType.STRING).description("????????? ??????").optional(),
          fieldWithPath("phone").type(JsonFieldType.STRING).description("????????????").optional(),
          fieldWithPath("license").type(JsonFieldType.STRING).description("????????? ??????").optional(),
          fieldWithPath("createdDateTime").type(JsonFieldType.VARIES).description("?????????"),
          fieldWithPath("lastModifiedDateTime").type(JsonFieldType.VARIES).description("?????? ?????????"),
          fieldWithPath("leaveDateTime").type(JsonFieldType.VARIES).description("?????????").optional(),
          fieldWithPath("authority").type(JsonFieldType.STRING).description("??????"),
          subsectionWithPath("address").type(JsonFieldType.OBJECT).description("??????").optional(),
          fieldWithPath("authProvider").type(JsonFieldType.STRING).description("?????? ????????? ??????").optional(),
          fieldWithPath("status").type(JsonFieldType.STRING).description("?????? ????????? ??????"),
          subsectionWithPath("_links").type(JsonFieldType.OBJECT).description("HATEOAS")
        )));
  }

  @Test
  @DisplayName("????????? ?????? ?????? ??????")
  @WithMockUser(roles = "ADMIN")
  void adminFindMembersTest() throws Exception {
    for (int i = 0; i < 5; i++) {
      SignupRequest signUpRequest = SignupRequest.builder()
        .email(String.format("panda-%d@naver.com", i + 1))
        .nickname("nick" + i + 1)
        .password("123456")
        .build();
      memberService.joinAsMember(signUpRequest);
    }

    mockMvc.perform(
        get("/api/members/admin")
          .accept(MediaType.APPLICATION_JSON_VALUE))
      .andDo(document("member/admin-get-members",
        relaxedResponseFields(
          subsectionWithPath("_embedded.memberQueryResponseList").type(JsonFieldType.ARRAY).description("?????? ?????????"),
          subsectionWithPath("_links").type(JsonFieldType.OBJECT).description("HATEOAS"),
          subsectionWithPath("page").type(JsonFieldType.OBJECT).description("????????? ??????")
        )));
  }
}