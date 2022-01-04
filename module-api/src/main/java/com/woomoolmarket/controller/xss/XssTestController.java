package com.woomoolmarket.controller.xss;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class XssTestController {

  @GetMapping(value = "/xss")
  public Map<String, Object> responseXss() {
    // java 11, factory method
    return Map.of("htmlTdTag", "<td></td>", "htmlTableTag", "<table>");
  }
}
