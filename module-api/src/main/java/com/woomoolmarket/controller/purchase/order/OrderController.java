package com.woomoolmarket.controller.purchase.order;


import com.woomoolmarket.entity.purchase.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/orders", produces = MediaTypes.HAL_JSON_VALUE)
public class OrderController {

    private final OrderRepository orderRepository;
}
