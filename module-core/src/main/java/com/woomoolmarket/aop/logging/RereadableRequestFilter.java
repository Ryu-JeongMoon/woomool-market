package com.woomoolmarket.aop.logging;

import com.woomoolmarket.util.constants.UriConstants;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RereadableRequestFilter extends OncePerRequestFilter {

  @Override
  protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
    return PatternMatchUtils.simpleMatch(UriConstants.SHOULD_NOT_FILTER_URL_PATTERNS, request.getRequestURI());
  }

  @Override
  protected void doFilterInternal(
    @NonNull HttpServletRequest request,
    @NonNull HttpServletResponse response,
    FilterChain filterChain) throws ServletException, IOException {

    RereadableRequest rereadableRequest = new RereadableRequest(request);
    filterChain.doFilter(rereadableRequest, response);
  }
}
