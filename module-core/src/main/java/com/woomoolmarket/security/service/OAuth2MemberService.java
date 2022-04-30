package com.woomoolmarket.security.service;

import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.security.oauth2.OAuth2Attributes;

public interface OAuth2MemberService {

  Member getMember(String registrationId, OAuth2Attributes attributes);
}