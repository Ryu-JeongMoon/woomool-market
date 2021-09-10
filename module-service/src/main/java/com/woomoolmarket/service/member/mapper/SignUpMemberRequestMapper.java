package com.woomoolmarket.service.member.mapper;

import com.woomoolmarket.common.mapper.GenericMapper;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.service.member.dto.request.SignUpMemberRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SignUpMemberRequestMapper extends GenericMapper<SignUpMemberRequest, Member> {

}
