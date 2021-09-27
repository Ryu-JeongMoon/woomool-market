package com.woomoolmarket.controller.member.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.woomoolmarket.controller.member.MemberController;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

@Log4j2
class MemberResponseModelTest {

    @Test
    @DisplayName("linkTo가 알맞은 경로를 반환한다")
    void linkToTest() {
        Link link = linkTo(methodOn(MemberController.class).getMember(1L)).withRel("hello");

        assertEquals(link.getHref(), "/api/members/1");
        assertEquals(link.getRel().toString(), "hello");
    }
}