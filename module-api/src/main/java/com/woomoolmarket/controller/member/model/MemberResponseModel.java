package com.woomoolmarket.controller.member.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.woomoolmarket.controller.member.MemberController;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class MemberResponseModel<M> extends EntityModel<MemberResponse> {

    public MemberResponseModel(MemberResponse memberResponse, Link... links) {
        super(memberResponse, links);
        add(linkTo(MemberController.class).slash(memberResponse.getId()).withSelfRel());
    }

    public static MemberResponseModel of(MemberResponse memberResponse, Iterable<Link> links) {
        return new MemberResponseModel(memberResponse, (Link) links);
    }
}
