package com.woomoolmarket.service.auth;

import static com.woomoolmarket.security.jwt.TokenConstant.ACCESS_TOKEN_EXPIRE_TIME;
import static com.woomoolmarket.security.jwt.TokenConstant.LOGIN_FAILED_KEY_PREFIX;
import static com.woomoolmarket.security.jwt.TokenConstant.LOGIN_KEY_PREFIX;
import static com.woomoolmarket.security.jwt.TokenConstant.LOGOUT_KEY_PREFIX;
import static com.woomoolmarket.security.jwt.TokenConstant.REFRESH_TOKEN_EXPIRE_TIME;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.common.util.ExceptionConstants;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.redis.RedisUtil;
import com.woomoolmarket.security.dto.TokenRequest;
import com.woomoolmarket.security.dto.TokenResponse;
import com.woomoolmarket.security.jwt.factory.TokenFactory;
import com.woomoolmarket.domain.member.dto.request.LoginRequest;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final RedisUtil redisUtil;
    private final TokenFactory tokenFactory;
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public TokenResponse login(LoginRequest loginRequest) {
        checkFailureCount(loginRequest);

        UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();
        Authentication authentication = authenticate(authenticationToken);
        TokenResponse tokenResponse = tokenFactory.createToken(authentication);

        String username = authentication.getName();
        String refreshToken = tokenResponse.getRefreshToken();
        redisUtil.setDataAndExpiration(LOGIN_KEY_PREFIX + username, refreshToken, REFRESH_TOKEN_EXPIRE_TIME);
        return tokenResponse;
    }

    private void checkFailureCount(LoginRequest loginRequest) {
        String loginFailureCount = redisUtil.getData(LOGIN_FAILED_KEY_PREFIX + loginRequest.getEmail());

        if (StringUtils.hasText(loginFailureCount) && Integer.parseInt(loginFailureCount) >= 5) {
            throw new AccessDeniedException(ExceptionConstants.MEMBER_BLOCKED);
        }
    }

    public Authentication authenticate(UsernamePasswordAuthenticationToken authenticationToken) {
        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            redisUtil.deleteData(LOGIN_FAILED_KEY_PREFIX + authenticationToken.getName());
            return authentication;
        } catch (AuthenticationException e) {
            memberRepository.findByEmailAndStatus(authenticationToken.getName(), Status.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionConstants.MEMBER_NOT_FOUND));
            redisUtil.increment(LOGIN_FAILED_KEY_PREFIX + authenticationToken.getName());
            throw e;
        }
    }

    public void logout(HttpServletRequest request) {
        String accessToken = tokenFactory.resolveTokenFrom(request);
        redisUtil.setDataAndExpiration(LOGOUT_KEY_PREFIX + accessToken, accessToken, ACCESS_TOKEN_EXPIRE_TIME);
        SecurityContextHolder.clearContext();
    }

    public TokenResponse reissue(TokenRequest tokenRequest) {
        String refreshToken = tokenRequest.getRefreshToken();
        Authentication authentication = tokenFactory.getAuthentication(tokenRequest.getAccessToken());
        String username = authentication.getName();

        if (!tokenFactory.validate(refreshToken) || !StringUtils.hasText(redisUtil.getData(LOGIN_KEY_PREFIX + username))) {
            throw new AccessDeniedException(ExceptionConstants.REFRESH_TOKEN_NOT_FOUND);
        }

        TokenResponse tokenResponse = tokenFactory.createToken(authentication);
        String reissuedRefreshToken = tokenResponse.getRefreshToken();
        redisUtil.setDataAndExpiration(LOGIN_KEY_PREFIX + username, reissuedRefreshToken, REFRESH_TOKEN_EXPIRE_TIME);
        return tokenResponse;
    }
}

// KEY_PREFIX -> TokenConstant 에 위치