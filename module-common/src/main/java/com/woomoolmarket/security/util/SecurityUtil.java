package com.woomoolmarket.security.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtil {

    private static final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    public static String getCurrentMemberEmail() {
        if (authentication == null || !StringUtils.hasText(authentication.getName())) {
            throw new RuntimeException("인증 정보가 존재하지 않습니다");
        }

        return authentication.getName();
    }
}
