package com.woomoolmarket.security.service;

import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.security.dto.UserPrincipal;
import com.woomoolmarket.security.oauth2.OAuth2Attributes;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2Service {

  default UserPrincipal getUserPrincipal(OAuth2UserRequest request, OAuth2MemberService oAuth2MemberService, OAuth2User user) {
    String registrationId = request.getClientRegistration().getRegistrationId();
    String attributeName = request.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

    OAuth2Attributes attributes = OAuth2Attributes.of(registrationId, attributeName, user.getAttributes());
    Member member = oAuth2MemberService.registerOrEdit(registrationId, attributes);

    return UserPrincipal.of(member, attributes.getAttributes());
  }
}
