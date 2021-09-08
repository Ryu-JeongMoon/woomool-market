package com.woomoolmarket.service.member.mapper;

import com.woomoolmarket.common.mapper.GenericMapper;
import com.woomoolmarket.entity.member.entity.Member;
import com.woomoolmarket.service.member.dto.response.SignUpMemberResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SignUpMemberResponseMapper extends GenericMapper<SignUpMemberResponse, Member> {}
