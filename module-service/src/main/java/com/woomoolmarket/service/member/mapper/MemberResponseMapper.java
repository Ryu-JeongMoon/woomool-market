package com.woomoolmarket.service.member.mapper;

import com.woomoolmarket.common.mapper.GenericMapper;
import com.woomoolmarket.model.member.entity.Member;
import com.woomoolmarket.service.member.dto.response.MemberResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberResponseMapper extends GenericMapper<MemberResponse, Member> {

}
