package com.woomoolmarket.controller;

import com.woomoolmarket.service.s3.S3Uploader;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Controller
@RequiredArgsConstructor
public class IndexController {

    private final S3Uploader s3Uploader;

    @GetMapping("/upload")
    public String uploadForm() {
        return "file";
    }

    @ResponseBody
    @PostMapping("/upload")
    public String upload(@RequestParam("data") MultipartFile multipartFile) throws IOException {
        return s3Uploader.upload(multipartFile, "static");
    }

    @ResponseBody
    @GetMapping("/request")
    public String httpCheck(HttpServletRequest request) {
        log.warn("http2 check ??!! -> {}", request.getProtocol());
        return request.getProtocol();
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @ResponseBody
    @GetMapping({"/loginSuccess", "/hello"})
    public String loginSuccess() {
        return "hello";
    }

    @ResponseBody
    @GetMapping("/loginFailure")
    public String loginFailure() {
        return "loginFailure";
    }
}
