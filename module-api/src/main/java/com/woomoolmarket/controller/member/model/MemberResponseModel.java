package com.woomoolmarket.controller.member.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.woomoolmarket.controller.member.MemberController;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class MemberResponseModel extends EntityModel<MemberResponse> {

    public MemberResponseModel(MemberResponse memberResponse, Link... links) {
        super(memberResponse, links);
    }
}
