package com.woomoolmarket.service.member.mapper;

import com.woomoolmarket.service.member.dto.request.SignUpMemberRequest;
import com.woomoolmarket.common.mapper.GenericMapper;
import com.woomoolmarket.entity.member.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SignUpMemberRequestMapper extends GenericMapper<SignUpMemberRequest, Member> {}
