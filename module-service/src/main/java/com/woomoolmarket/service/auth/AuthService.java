package com.woomoolmarket.service.auth;

import com.woomoolmarket.common.util.ExceptionUtil;
import com.woomoolmarket.domain.token.entiy.RefreshToken;
import com.woomoolmarket.domain.token.repository.RefreshTokenRepository;
import com.woomoolmarket.redis.RedisUtil;
import com.woomoolmarket.security.dto.TokenRequest;
import com.woomoolmarket.security.dto.TokenResponse;
import com.woomoolmarket.security.jwt.TokenProvider;
import com.woomoolmarket.service.member.dto.request.LoginRequest;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private static final long EXPIRED_DURATION = 60 * 30; // 30minutes
    private static final String REDIS_KEY_PREFIX = "logout:";

    private final RedisUtil redisUtil;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public TokenResponse login(LoginRequest loginRequest) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenResponse tokenResponse = tokenProvider.createToken(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
            .key(authentication.getName())
            .value(tokenResponse.getRefreshToken())
            .build();

        refreshTokenRepository.save(refreshToken);

        // 5. 토큰 발급
        return tokenResponse;
    }

    // 레디스에 액세스 토큰 넣기
    public void logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        String accessToken = tokenProvider.resolveTokenFrom(request);
        redisUtil.setDataExpire(REDIS_KEY_PREFIX + accessToken, accessToken, EXPIRED_DURATION);
    }

    public TokenResponse reissue(TokenRequest tokenRequest) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validate(tokenRequest.getRefreshToken())) {
            throw new AccessDeniedException(ExceptionUtil.REFRESH_TOKEN_NOT_FOUND);
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequest.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
            .orElseThrow(() -> new AccessDeniedException(ExceptionUtil.USER_NOT_LOGIN));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequest.getRefreshToken())) {
            throw new AccessDeniedException(ExceptionUtil.REFRESH_TOKEN_NOT_FOUND);
        }

        // 5. 새로운 토큰 생성
        TokenResponse tokenResponse = tokenProvider.createToken(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenResponse.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenResponse;
    }
}

// 어렵당 우헤헤