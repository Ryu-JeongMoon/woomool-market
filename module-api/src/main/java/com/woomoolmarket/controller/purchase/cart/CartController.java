package com.woomoolmarket.controller.purchase.cart;

import com.woomoolmarket.entity.purchase.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/carts", produces = MediaTypes.HAL_JSON_VALUE)
public class CartController {

    private final CartRepository cartRepository;
}
