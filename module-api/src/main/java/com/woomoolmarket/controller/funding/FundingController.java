package com.woomoolmarket.controller.funding;

import com.woomoolmarket.aop.time.LogExecutionTime;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@LogExecutionTime
@RequiredArgsConstructor
@RequestMapping(path = "/api/funding", produces = MediaTypes.HAL_JSON_VALUE)
public class FundingController {

}
