package com.woomoolmarket.controller.order;


import com.woomoolmarket.aop.time.LogExecutionTime;
import com.woomoolmarket.domain.purchase.order.repository.OrderSearchCondition;
import com.woomoolmarket.service.order.OrderService;
import com.woomoolmarket.service.order.dto.request.OrderDeleteRequest;
import com.woomoolmarket.service.order.dto.request.OrderRequest;
import com.woomoolmarket.service.order.dto.response.OrderResponse;
import com.woomoolmarket.util.PageUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@LogExecutionTime
@RequiredArgsConstructor
@RequestMapping(path = "/api/orders", produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
public class OrderController {

    private final OrderService orderService;
    private final PagedResourcesAssembler<OrderResponse> assembler;

    @GetMapping
    @PreAuthorize("@checker.isSelf(#memberId) or hasRole('ROLE_ADMIN')")
    public ResponseEntity<PagedModel<EntityModel<OrderResponse>>> getOrders(
        @RequestParam Long memberId, @PageableDefault Pageable pageable) {

        List<OrderResponse> orderResponses = orderService.getListByMemberId(memberId);
        Page<OrderResponse> responsePage = PageUtil.toPage(orderResponses, pageable);
        return ResponseEntity.ok(assembler.toModel(responsePage));
    }

    @PostMapping
    @PreAuthorize("@checker.isSelf(#orderRequest.memberId)")
    public ResponseEntity createOrder(@RequestBody OrderRequest orderRequest) {
        orderService.order(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    @PreAuthorize("@checker.isSelf(#deleteRequest.memberId) or hasRole('ROLE_ADMIN')")
    public ResponseEntity cancelOrder(@RequestBody OrderDeleteRequest deleteRequest) {
        orderService.cancel(deleteRequest.getOrderId());
        return ResponseEntity.noContent().build();
    }


    /* FOR ADMIN */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PagedModel<EntityModel<OrderResponse>>> getListBySearchConditionForAdmin(
        OrderSearchCondition condition, @PageableDefault Pageable pageable) {

        List<OrderResponse> orderResponses = orderService.getListBySearchCondition(condition);
        Page<OrderResponse> responsePage = PageUtil.toPage(orderResponses, pageable);
        return ResponseEntity.ok().body(assembler.toModel(responsePage));
    }
}