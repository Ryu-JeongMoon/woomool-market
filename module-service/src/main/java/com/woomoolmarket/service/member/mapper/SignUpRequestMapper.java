package com.woomoolmarket.service.member.mapper;

import com.woomoolmarket.common.mapper.GenericMapper;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.service.member.dto.request.SignUpRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SignUpRequestMapper extends GenericMapper<SignUpRequest, Member> {

}
