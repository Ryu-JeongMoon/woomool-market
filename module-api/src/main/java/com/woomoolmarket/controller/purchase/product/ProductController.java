package com.woomoolmarket.controller.purchase.product;

import com.woomoolmarket.entity.purchase.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/board", produces = MediaTypes.HAL_JSON_VALUE)
public class ProductController {

    private final ProductRepository productRepository;
}
