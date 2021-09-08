package com.woomoolmarket.controller.member.model;

import com.woomoolmarket.controller.member.MemberController;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class MemberResponseModel extends EntityModel<MemberResponse> {

    public MemberResponseModel(MemberResponse memberResponse, Link... links) {
        super(memberResponse, links);
        add(linkTo(MemberController.class).slash(memberResponse.getId()).withSelfRel());
    }
}
