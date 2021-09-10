package com.woomoolmarket.controller.member.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.woomoolmarket.controller.member.MemberController;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.service.member.dto.response.SignUpMemberResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class MemberModel extends EntityModel<Member> {

    public MemberModel(Member member, Link... links) {
        super(member, links);
        add(linkTo(MemberController.class).slash(member.getId()).withSelfRel());
    }

    /* TODO SignUpMemberResponse 가 꼭 필요한 걸까? 고민해봅시다  */
    public MemberModel(SignUpMemberResponse signUpMemberResponse, Link... links) {
        add(linkTo(MemberController.class).slash(signUpMemberResponse.getId()).withSelfRel());
    }
}
