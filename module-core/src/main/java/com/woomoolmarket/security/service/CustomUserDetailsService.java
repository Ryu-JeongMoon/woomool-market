package com.woomoolmarket.security.service;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.common.util.ExceptionConstants;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.security.dto.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return memberRepository.findByEmailAndStatus(email, Status.ACTIVE)
            .map(this::createUserDetails)
            .orElseThrow(() -> new UsernameNotFoundException(ExceptionConstants.MEMBER_NOT_FOUND));
    }

    private UserPrincipal createUserDetails(Member member) {
        return UserPrincipal.of(member);
    }
}
