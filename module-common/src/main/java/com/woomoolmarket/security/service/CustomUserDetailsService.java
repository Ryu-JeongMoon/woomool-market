package com.woomoolmarket.security.service;

import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.entity.MemberStatus;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String email) {
        return memberRepository.findByEmail(email)
            .map(this::createUserDetails)
            .orElseThrow(() -> new UsernameNotFoundException(email + " -> 존재하지 않는 회원 이름입니다."));
    }

    private User createUserDetails(Member member) {
        if (member.getMemberStatus() == MemberStatus.INACTIVE) {
            throw new RuntimeException(member.getEmail() + " -> 탈퇴한 회원입니다.");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(member.getAuthority().toString()));

//            member.getAuthority().stream()
//            .map(authority -> new SimpleGrantedAuthority(authority.toString()))
//            .collect(Collectors.toList());

        return new User(member.getEmail(), member.getPassword(), grantedAuthorities);
    }
}
