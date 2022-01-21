package com.woomoolmarket.service.auth;

import static com.woomoolmarket.common.constants.CacheConstants.LOGIN_ACCESS_TOKEN_PREFIX;
import static com.woomoolmarket.common.constants.CacheConstants.LOGIN_FAILED_KEY_PREFIX;
import static com.woomoolmarket.common.constants.CacheConstants.LOGIN_REFRESH_TOKEN_PREFIX;
import static com.woomoolmarket.common.constants.CacheConstants.LOGOUT_KEY_PREFIX;
import static com.woomoolmarket.common.constants.CacheConstants.MAXIMAL_NUMBER_OF_WRONG_PASSWORD;
import static com.woomoolmarket.common.constants.TokenConstants.ACCESS_TOKEN_EXPIRE_SECONDS;
import static com.woomoolmarket.common.constants.TokenConstants.REFRESH_TOKEN_EXPIRE_SECONDS;

import com.woomoolmarket.cache.CacheService;
import com.woomoolmarket.common.constants.ExceptionConstants;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.common.util.TokenUtils;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.security.dto.TokenRequest;
import com.woomoolmarket.security.dto.TokenResponse;
import com.woomoolmarket.security.jwt.factory.TokenCreator;
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
  private final TokenCreator tokenCreator;
  private final MemberRepository memberRepository;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;

  @Transactional
  public TokenResponse login(LoginRequest loginRequest) {
    checkFailureCount(loginRequest);

    UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthenticationToken();
    Authentication authentication = authenticate(authenticationToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    TokenResponse tokenResponse = tokenCreator.createToken(authentication);
    saveTokensToCache(authentication.getName(), tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());

    return tokenResponse;
  }

  private void saveTokensToCache(String username, String accessToken, String refreshToken) {
    cacheService.setDataAndExpiration(LOGIN_ACCESS_TOKEN_PREFIX + username, accessToken, ACCESS_TOKEN_EXPIRE_SECONDS);
    cacheService.setDataAndExpiration(LOGIN_REFRESH_TOKEN_PREFIX + username, refreshToken, REFRESH_TOKEN_EXPIRE_SECONDS);
  }

  private void checkFailureCount(LoginRequest loginRequest) {
    String loginFailureCount = cacheService.getData(LOGIN_FAILED_KEY_PREFIX + loginRequest.getEmail());

    if (StringUtils.hasText(loginFailureCount) && Integer.parseInt(loginFailureCount) >= MAXIMAL_NUMBER_OF_WRONG_PASSWORD) {
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
    if (!tokenCreator.validate(accessToken)) {
      throw new IllegalArgumentException(ExceptionConstants.ACCESS_TOKEN_NOT_VALID);
    }
    cacheService.setDataAndExpiration(LOGOUT_KEY_PREFIX + accessToken, accessToken, ACCESS_TOKEN_EXPIRE_SECONDS);
    SecurityContextHolder.clearContext();
  }

  @Transactional
  public TokenResponse reissue(TokenRequest tokenRequest) {
    String refreshToken = tokenRequest.getRefreshToken();
    Authentication authentication = tokenCreator.getAuthentication(tokenRequest.getAccessToken());
    String username = authentication.getName();

    if (!tokenCreator.validate(refreshToken) || !StringUtils.hasText(
      cacheService.getData(LOGIN_REFRESH_TOKEN_PREFIX + username))) {
      throw new AccessDeniedException(ExceptionConstants.REFRESH_TOKEN_NOT_VALID);
    }

    TokenResponse tokenResponse = tokenCreator.createToken(authentication);
    String reissuedRefreshToken = tokenResponse.getRefreshToken();
    cacheService.setDataAndExpiration(LOGIN_REFRESH_TOKEN_PREFIX + username, reissuedRefreshToken, REFRESH_TOKEN_EXPIRE_SECONDS);
    return tokenResponse;
  }
}