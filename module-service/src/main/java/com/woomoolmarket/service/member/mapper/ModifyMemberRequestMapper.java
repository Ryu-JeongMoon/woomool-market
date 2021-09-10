package com.woomoolmarket.service.member.mapper;

import com.woomoolmarket.common.mapper.GenericMapper;
import com.woomoolmarket.model.member.entity.Member;
import com.woomoolmarket.service.member.dto.request.ModifyMemberRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ModifyMemberRequestMapper extends GenericMapper<ModifyMemberRequest, Member> {

}
