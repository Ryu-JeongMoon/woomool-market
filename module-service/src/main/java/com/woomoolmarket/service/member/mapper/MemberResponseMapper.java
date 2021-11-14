package com.woomoolmarket.service.member.mapper;

import com.woomoolmarket.common.mapper.GenericMapper;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.dto.response.MemberResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberResponseMapper extends GenericMapper<MemberResponse, Member> {

}
