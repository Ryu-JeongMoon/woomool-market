package com.woomoolmarket.security.service;

import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.entity.enumeration.AuthProvider;
import com.woomoolmarket.domain.repository.MemberRepository;
import com.woomoolmarket.security.dto.UserPrincipal;
import com.woomoolmarket.util.constants.ExceptionMessages;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String email) {
    Optional<Member> probableMember = memberRepository.findActiveByEmail(email);
    probableMember.ifPresent(member -> member.changeLatestAuthProvider(AuthProvider.LOCAL));

    return probableMember
      .map(UserPrincipal::from)
      .orElseThrow(() -> new UsernameNotFoundException(ExceptionMessages.Member.NOT_FOUND));
  }
}
