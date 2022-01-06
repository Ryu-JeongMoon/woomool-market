package com.woomoolmarket.security.service;

import com.woomoolmarket.common.constants.ExceptionConstants;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.member.entity.AuthProvider;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.security.oauth2.OAuth2Attributes;
import javax.persistence.EntityListeners;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@EntityListeners(value = AuditingEntityListener.class)
public class OAuth2MemberService {

  private final MemberRepository memberRepository;

  @Transactional
  public Member registerOrEdit(String registrationId, OAuth2Attributes attributes) {
    if (memberRepository.findByEmailAndStatus(attributes.getEmail(), Status.ACTIVE).isEmpty()) {
      return registerNewMember(registrationId, attributes);
    }

    return editExistingMember(registrationId, attributes);
  }

  @Transactional
  public Member registerNewMember(String registrationId, OAuth2Attributes attributes) {
    Member member = Member.builder()
      .email(attributes.getEmail())
      .nickname(attributes.getNickname())
      .profileImage(attributes.getProfileImage())
      .authProvider(AuthProvider.valueOf(registrationId.toUpperCase()))
      .build();

    return memberRepository.save(member);
  }

  @Transactional
  public Member editExistingMember(String registrationId, OAuth2Attributes attributes) {
    return memberRepository.findByEmail(attributes.getEmail())
      .orElseThrow(() -> new EntityNotFoundException(ExceptionConstants.MEMBER_NOT_FOUND))
      .editByOAuth2(attributes.getNickname(), attributes.getProfileImage(), AuthProvider.valueOf(registrationId.toUpperCase()));
  }
}
