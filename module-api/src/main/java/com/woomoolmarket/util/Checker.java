package com.woomoolmarket.util;

import com.woomoolmarket.security.dto.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class Checker {

    public boolean isSelf(Long memberId, Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return memberId == principal.getId();
    }
}
