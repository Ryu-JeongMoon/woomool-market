package com.woomoolmarket.service.auth;

import static com.woomoolmarket.util.constants.CacheConstants.LOGIN_ACCESS_TOKEN_PREFIX;
import static com.woomoolmarket.util.constants.CacheConstants.LOGIN_FAILED_KEY_PREFIX;
import static com.woomoolmarket.util.constants.CacheConstants.LOGIN_REFRESH_TOKEN_PREFIX;
import static com.woomoolmarket.util.constants.CacheConstants.LOGOUT_KEY_PREFIX;
import static com.woomoolmarket.util.constants.CacheConstants.MAXIMAL_NUMBER_OF_WRONG_PASSWORD;

import com.woomoolmarket.domain.entity.enumeration.Status;
import com.woomoolmarket.domain.port.CacheTokenPort;
import com.woomoolmarket.domain.repository.MemberRepository;
import com.woomoolmarket.security.dto.TokenRequest;
import com.woomoolmarket.security.dto.TokenResponse;
import com.woomoolmarket.security.jwt.factory.TokenFactory;
import com.woomoolmarket.security.jwt.verifier.TokenVerifier;
import com.woomoolmarket.service.member.dto.request.LoginRequest;
import com.woomoolmarket.util.TokenUtils;
import com.woomoolmarket.util.constants.ExceptionMessages;
import com.woomoolmarket.util.constants.Times;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

  private final CacheTokenPort cacheTokenPort;
  private final TokenFactory tokenFactory;
  private final TokenVerifier tokenVerifier;
  private final MemberRepository memberRepository;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;

  @Transactional
  public TokenResponse login(LoginRequest loginRequest) {
    checkFailureCount(loginRequest);

    UsernamePasswordAuthenticationToken authenticationToken = loginRequest.toAuthenticationToken();
    Authentication authentication = authenticate(authenticationToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    TokenResponse tokenResponse = tokenFactory.create(authentication);
    saveTokensToCache(authentication.getName(), tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());

    return tokenResponse;
  }

  private void saveTokensToCache(String username, String accessToken, String refreshToken) {
    cacheTokenPort.setDataAndExpiration(LOGIN_ACCESS_TOKEN_PREFIX + username, accessToken, Times.ACCESS_TOKEN_EXPIRATION_SECONDS.getValue());
    cacheTokenPort.setDataAndExpiration(LOGIN_REFRESH_TOKEN_PREFIX + username, refreshToken, Times.REFRESH_TOKEN_EXPIRATION_SECONDS.getValue());
  }

  private void checkFailureCount(LoginRequest loginRequest) {
    String loginFailureCount = cacheTokenPort.getData(LOGIN_FAILED_KEY_PREFIX + loginRequest.getEmail());

    if (StringUtils.hasText(loginFailureCount) && Integer.parseInt(loginFailureCount) >= MAXIMAL_NUMBER_OF_WRONG_PASSWORD) {
      throw new AccessDeniedException(ExceptionMessages.Member.BLOCKED);
    }
  }

  @Transactional
  public Authentication authenticate(UsernamePasswordAuthenticationToken authenticationToken) {
    try {
      Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
      cacheTokenPort.deleteData(LOGIN_FAILED_KEY_PREFIX + authenticationToken.getName());
      return authentication;
    } catch (AuthenticationException e) {
      memberRepository.findByEmailAndStatus(authenticationToken.getName(), Status.ACTIVE)
        .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.Member.NOT_FOUND));
      cacheTokenPort.increment(LOGIN_FAILED_KEY_PREFIX + authenticationToken.getName());

      log.info("[WOOMOOL-ERROR] :: Invalid Token => {} ", e.getMessage());
      throw e;
    }
  }

  @Transactional
  public void logout(HttpServletRequest request) {
    String accessToken = TokenUtils.resolveAccessToken(request);
    if (!tokenVerifier.isValid(accessToken)) {
      throw new IllegalArgumentException(ExceptionMessages.Token.NOT_VALID);
    }
    cacheTokenPort.setDataAndExpiration(LOGOUT_KEY_PREFIX + accessToken, accessToken, Times.ACCESS_TOKEN_EXPIRATION_SECONDS.getValue());
    SecurityContextHolder.clearContext();
  }

  @Transactional
  public TokenResponse reissue(TokenRequest tokenRequest) {
    String refreshToken = tokenRequest.getRefreshToken();
    Authentication authentication = tokenVerifier.getAuthentication(tokenRequest.getAccessToken());
    String username = authentication.getName();

    if (!tokenVerifier.isValid(refreshToken) || !StringUtils.hasText(cacheTokenPort.getData(LOGIN_REFRESH_TOKEN_PREFIX + username))) {
      throw new AccessDeniedException(ExceptionMessages.Token.NOT_VALID);
    }

    TokenResponse tokenResponse = tokenFactory.create(authentication);
    String reissuedRefreshToken = tokenResponse.getRefreshToken();
    cacheTokenPort.setDataAndExpiration(LOGIN_REFRESH_TOKEN_PREFIX + username, reissuedRefreshToken, Times.REFRESH_TOKEN_EXPIRATION_SECONDS.getValue());
    return tokenResponse;
  }
}