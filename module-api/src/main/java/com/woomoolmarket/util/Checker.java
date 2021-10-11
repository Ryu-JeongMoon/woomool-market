package com.woomoolmarket.util;

import com.woomoolmarket.common.util.ExceptionUtil;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.security.dto.UserPrincipal;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Checker {

    private final MemberRepository memberRepository;

    public boolean isSelf(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.MEMBER_NOT_FOUND));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        return member.getEmail().equals(principal.getUsername());
    }
}