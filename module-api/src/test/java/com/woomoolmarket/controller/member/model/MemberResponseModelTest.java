package com.woomoolmarket.controller.member.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.woomoolmarket.controller.member.MemberController;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.Link;

@Log4j2
@ExtendWith(MockitoExtension.class)
class MemberResponseModelTest {

    @Test
    @DisplayName("linkTo가 알맞은 경로를 반환한다")
    void linkToTest() {
        Link link = linkTo(methodOn(MemberController.class).getMember(1L)).withRel("hello");

        assertThat(link.getHref()).isEqualTo("/api/members/1");
        assertThat(link.getRel().toString()).isEqualTo("hello");
    }
}