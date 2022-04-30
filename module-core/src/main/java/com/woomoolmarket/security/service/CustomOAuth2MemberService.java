package com.woomoolmarket.security.service;

import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.entity.enumeration.AuthProvider;
import com.woomoolmarket.domain.entity.enumeration.Status;
import com.woomoolmarket.domain.repository.MemberRepository;
import com.woomoolmarket.security.oauth2.OAuth2Attributes;
import com.woomoolmarket.util.constants.ExceptionMessages;
import java.util.Optional;
import javax.persistence.EntityListeners;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@EntityListeners(value = AuditingEntityListener.class)
public class CustomOAuth2MemberService implements OAuth2MemberService {

  private final MemberRepository memberRepository;

  public Member getMember(String registrationId, OAuth2Attributes attributes) {
    Optional<Member> probableMember = memberRepository.findByEmail(attributes.getEmail());
    probableMember.ifPresent(member -> {
        throwIfInactive(member);
        member.changeLatestAuthProvider(AuthProvider.valueOfCaseInsensitively(registrationId));
      }
    );

    return probableMember.orElseGet(() -> getNewlyRegistered(registrationId, attributes));
  }

  private void throwIfInactive(Member member) {
    if (member.getStatus() == Status.INACTIVE)
      throw new IllegalStateException(ExceptionMessages.Member.ALREADY_LEFT);
  }

  private Member getNewlyRegistered(String registrationId, OAuth2Attributes oAuth2Attributes) {
    Member member = oAuth2Attributes.toMember(registrationId);
    return memberRepository.save(member);
  }
}
