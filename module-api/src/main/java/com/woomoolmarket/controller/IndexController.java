package com.woomoolmarket.controller;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Log4j2
@Controller
public class IndexController {

    @ResponseBody
    @GetMapping("/request")
    public String httpCheck(HttpServletRequest request) {
        log.warn("http2 check ??!! -> {}", request.getProtocol());
        return request.getProtocol();
    }

    @GetMapping("/")
    public String getAuthorizationMessage() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping({"/loginSuccess", "/hello"})
    public String loginSuccess() {
        return "hello";
    }

    @GetMapping("/loginFailure")
    public String loginFailure() {
        return "loginFailure";
    }
}
