package com.woomoolmarket.service.auth;

import static com.woomoolmarket.common.constants.CacheConstants.LOGIN_FAILED_KEY_PREFIX;
import static com.woomoolmarket.common.constants.CacheConstants.LOGIN_KEY_PREFIX;
import static com.woomoolmarket.common.constants.CacheConstants.LOGOUT_KEY_PREFIX;
import static com.woomoolmarket.common.constants.TokenConstants.ACCESS_TOKEN_EXPIRE_SECONDS;
import static com.woomoolmarket.common.constants.TokenConstants.REFRESH_TOKEN_EXPIRE_SECONDS;

import com.woomoolmarket.cache.CacheService;
import com.woomoolmarket.common.constants.ExceptionConstants;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.common.util.TokenUtils;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.security.dto.TokenRequest;
import com.woomoolmarket.security.dto.TokenResponse;
import com.woomoolmarket.security.jwt.factory.TokenFactory;
import com.woomoolmarket.service.member.dto.request.LoginRequest;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthService {

    private final CacheService cacheService;
    private final TokenFactory tokenFactory;
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public TokenResponse login(LoginRequest loginRequest) {
        checkFailureCount(loginRequest);

        UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();
        Authentication authentication = authenticate(authenticationToken);
        TokenResponse tokenResponse = tokenFactory.createToken(authentication);

        String username = authentication.getName();
        String refreshToken = tokenResponse.getRefreshToken();
        cacheService.setDataAndExpiration(LOGIN_KEY_PREFIX + username, refreshToken, REFRESH_TOKEN_EXPIRE_SECONDS);
        return tokenResponse;
    }

    private void checkFailureCount(LoginRequest loginRequest) {
        String loginFailureCount = cacheService.getData(LOGIN_FAILED_KEY_PREFIX + loginRequest.getEmail());

        if (StringUtils.hasText(loginFailureCount) && Integer.parseInt(loginFailureCount) >= 5) {
            throw new AccessDeniedException(ExceptionConstants.MEMBER_BLOCKED);
        }
    }

    @Transactional
    public Authentication authenticate(UsernamePasswordAuthenticationToken authenticationToken) {
        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            cacheService.deleteData(LOGIN_FAILED_KEY_PREFIX + authenticationToken.getName());
            return authentication;
        } catch (AuthenticationException e) {
            memberRepository.findByEmailAndStatus(authenticationToken.getName(), Status.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionConstants.MEMBER_NOT_FOUND));
            cacheService.increment(LOGIN_FAILED_KEY_PREFIX + authenticationToken.getName());
            log.info("[WOOMOOL-ERROR] :: Invalid Token => {} ", e.getMessage());
            throw e;
        }
    }

    @Transactional
    public void logout(HttpServletRequest request) {
        String accessToken = TokenUtils.resolveAccessTokenFrom(request);
        cacheService.setDataAndExpiration(LOGOUT_KEY_PREFIX + accessToken, accessToken, ACCESS_TOKEN_EXPIRE_SECONDS);
        SecurityContextHolder.clearContext();
    }

    @Transactional
    public TokenResponse reissue(TokenRequest tokenRequest) {
        String refreshToken = tokenRequest.getRefreshToken();
        Authentication authentication = tokenFactory.getAuthentication(tokenRequest.getAccessToken());
        String username = authentication.getName();

        if (!tokenFactory.validate(refreshToken) || !StringUtils.hasText(cacheService.getData(LOGIN_KEY_PREFIX + username))) {
            throw new AccessDeniedException(ExceptionConstants.REFRESH_TOKEN_NOT_FOUND);
        }

        TokenResponse tokenResponse = tokenFactory.createToken(authentication);
        String reissuedRefreshToken = tokenResponse.getRefreshToken();
        cacheService.setDataAndExpiration(LOGIN_KEY_PREFIX + username, reissuedRefreshToken, REFRESH_TOKEN_EXPIRE_SECONDS);
        return tokenResponse;
    }
}