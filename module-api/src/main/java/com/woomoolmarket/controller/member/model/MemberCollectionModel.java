package com.woomoolmarket.controller.member.model;

import com.woomoolmarket.service.member.dto.response.MemberResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;

public class MemberCollectionModel extends CollectionModel<MemberResponse> {

    public MemberCollectionModel(Iterable<MemberResponse> content, Link... links) {
        super(content, links);
    }
}
