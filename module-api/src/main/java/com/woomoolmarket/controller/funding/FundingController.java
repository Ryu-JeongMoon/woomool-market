package com.woomoolmarket.controller.funding;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/funding", produces = MediaTypes.HAL_JSON_VALUE)
public class FundingController {
}
