package com.woomoolmarket.aop;

import com.nimbusds.jose.shaded.json.JSONObject;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
@Profile(value = { "local", "mysql", "ec2" })
public class LogAspect {

  @Around(value = "bean(*Controller)")
  public Object logForRequest(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    Object result = proceedingJoinPoint.proceed();
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

    String methodName = proceedingJoinPoint.getSignature().getName();
    String controllerName = proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName();

    Map<String, Object> params = new LinkedHashMap<>();
    params.put("http-method", request.getMethod());
    params.put("request-uri", request.getRequestURI());
    params.put("controller", controllerName);
    params.put("method", methodName);
    params.put("params", getParams(request));
    params.put("log-time", LocalDateTime.now());

    log.info("[WOOMOOL-REQUEST] :: {}", params);
    return result;
  }

  private static JSONObject getParams(HttpServletRequest request) {
    JSONObject jsonObject = new JSONObject();
    Enumeration<String> params = request.getParameterNames();
    while (params.hasMoreElements()) {
      String param = params.nextElement();
      String replaceParam = param.replaceAll("\\.", "-");
      jsonObject.put(replaceParam, request.getParameter(param));
    }
    return jsonObject;
  }

  @Around(value = "bean(*Controller)")
  public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start(String.valueOf(joinPoint.getSignature()));
    Object proceed = joinPoint.proceed();
    stopWatch.stop();

    log.info("[WOOMOOL-STOPWATCH] :: -> {}", stopWatch.prettyPrint());
    return proceed;
  }

  @AfterReturning(value = "@within(com.woomoolmarket.aop.annotation.LogForException)", returning = "response")
  public void logForException(JoinPoint joinPoint, Object response) {
    log.info("[WOOMOOL-ERROR] :: method -> {}", joinPoint.getSignature().toShortString());
    log.info("[WOOMOOL-ERROR] :: target -> {}", joinPoint.getTarget());
    log.info("[WOOMOOL-ERROR] :: response -> {}", response);
  }
}
