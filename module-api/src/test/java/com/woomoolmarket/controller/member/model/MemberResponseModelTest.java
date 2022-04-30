package com.woomoolmarket.controller.member.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.woomoolmarket.controller.member.MemberController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

@Slf4j
class MemberResponseModelTest {

  @Test
  @DisplayName("linkTo가 알맞은 경로를 반환한다")
  void linkToTest() {
    Link link = linkTo(methodOn(MemberController.class).getBy(1L)).withRel("hello");

    assertEquals(link.getHref(), "/api/members/1");
    assertEquals(link.getRel().toString(), "hello");
  }
}