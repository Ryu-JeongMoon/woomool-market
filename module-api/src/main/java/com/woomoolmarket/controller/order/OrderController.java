package com.woomoolmarket.controller.order;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.service.order.OrderService;
import com.woomoolmarket.service.order.dto.request.ModifyOrderRequest;
import com.woomoolmarket.service.order.dto.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Transactional(readOnly = true)
@RequestMapping(path = "/api/orders", produces = MediaTypes.HAL_JSON_VALUE)
public class OrderController {

    private final ObjectMapper objectMapper;
    private final OrderService orderService;
    private final PagedResourcesAssembler<OrderResponse> assembler;

    @GetMapping("/{memberId}")
    public ResponseEntity<PagedModel<EntityModel<OrderResponse>>> getOrders(@PathVariable Long memberId,
        @PageableDefault Pageable pageable) {

        Page<OrderResponse> pagedResponse = orderService.findOrdersByMemberId(memberId, pageable);
        return ResponseEntity.ok(assembler.toModel(pagedResponse));
    }

    @PostMapping("/{memberId}")
    public ResponseEntity createOrder(@PathVariable Long memberId,
        @RequestParam(value = "productId", required = false, defaultValue = "0") Long productId, int quantity) {
        if (productId != 0) {
            orderService.orderOne(memberId, productId, quantity);
        } else {
            orderService.orderMultiples(memberId);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{memberId}/{orderId}")
    public ResponseEntity editOrderInfo(@PathVariable Long memberId, @PathVariable Long orderId,
        @Validated @RequestBody ModifyOrderRequest modifyRequest, BindingResult bindingResult) throws JsonProcessingException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(bindingResult));
        }
        Link createUri = linkTo(methodOn(OrderController.class).getOrders(memberId, Pageable.unpaged())).withSelfRel();
        OrderResponse orderResponse = orderService.edit(orderId, modifyRequest);
        EntityModel<OrderResponse> responseModel = EntityModel.of(orderResponse, createUri);

        return ResponseEntity.created(createUri.toUri()).body(responseModel);
    }

    @DeleteMapping("/{memberId}/{orderId}")
    public ResponseEntity cancelOrder(@PathVariable Long memberId, @PathVariable Long orderId) {
        orderService.cancel(orderId);
        return ResponseEntity.noContent().build();
    }


    /* FOR ADMIN */
    @GetMapping("/admin-only/{memberId}")
    public ResponseEntity<PagedModel<EntityModel<OrderResponse>>> getOrdersByAdminAndMemberId(
        @PathVariable Long memberId, @PageableDefault Pageable pageable) {
        Page<OrderResponse> pagedResponse = orderService.findOrdersByMemberId(memberId, pageable);
        return ResponseEntity.ok().body(assembler.toModel(pagedResponse));
    }

    @GetMapping("/admin-only/all")
    public ResponseEntity<PagedModel<EntityModel<OrderResponse>>> getAllOrdersByAdmin(@PageableDefault Pageable pageable) {
        Page<OrderResponse> pagedResponse = orderService.findAllOrders(pageable);
        return ResponseEntity.ok().body(assembler.toModel(pagedResponse));
    }

    @GetMapping("/admin-only/active")
    public ResponseEntity<PagedModel<EntityModel<OrderResponse>>> getActiveOrdersByAdmin(@PageableDefault Pageable pageable) {
        Page<OrderResponse> pagedResponse = orderService.findAllActiveOrders(pageable);
        return ResponseEntity.ok().body(assembler.toModel(pagedResponse));
    }

    @GetMapping("/admin-only/inactive")
    public ResponseEntity<PagedModel<EntityModel<OrderResponse>>> getInactiveOrdersByAdmin(@PageableDefault Pageable pageable) {
        Page<OrderResponse> pagedResponse = orderService.findAllInactiveOrders(pageable);
        return ResponseEntity.ok().body(assembler.toModel(pagedResponse));
    }
}

/*
URL 에 멤버 번호 노출되는게 좋지는 않은거 같은데 개선할 수 있을까?
권한 설정 필요
 */