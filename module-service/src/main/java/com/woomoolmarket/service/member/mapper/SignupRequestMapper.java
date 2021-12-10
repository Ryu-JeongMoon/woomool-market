package com.woomoolmarket.service.member.mapper;

import com.woomoolmarket.common.mapper.GenericMapper;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.service.member.dto.request.SignupRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SignupRequestMapper extends GenericMapper<SignupRequest, Member> {

}
