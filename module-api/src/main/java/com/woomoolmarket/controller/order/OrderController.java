package com.woomoolmarket.controller.order;


import com.woomoolmarket.aop.time.LogExecutionTime;
import com.woomoolmarket.domain.purchase.order.repository.OrderSearchCondition;
import com.woomoolmarket.service.order.OrderService;
import com.woomoolmarket.service.order.dto.response.OrderResponse;
import com.woomoolmarket.util.PageUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@LogExecutionTime
@RestController
@RequiredArgsConstructor
@Transactional(readOnly = true)
@RequestMapping(path = "/api/orders", produces = MediaTypes.HAL_JSON_VALUE)
public class OrderController {

    private final OrderService orderService;
    private final PagedResourcesAssembler<OrderResponse> assembler;

    @GetMapping("/{memberId}")
    @PreAuthorize("@checker.isSelf(#memberId) or hasRole('ROLE_ADMIN')")
    public ResponseEntity<PagedModel<EntityModel<OrderResponse>>> getOrders(
        @PathVariable Long memberId, @PageableDefault Pageable pageable) {

        List<OrderResponse> orderResponses = orderService.getListByMemberId(memberId);
        Page<OrderResponse> responsePage = PageUtil.toPage(orderResponses, pageable);
        return ResponseEntity.ok(assembler.toModel(responsePage));
    }

    @PostMapping("/{memberId}")
    @PreAuthorize("@checker.isSelf(#memberId)")
    public ResponseEntity createOrder(@PathVariable Long memberId,
        @RequestParam(value = "productId", required = false, defaultValue = "0") Long productId, int quantity) {
        if (productId != 0) {
            orderService.orderOne(memberId, productId, quantity);
        } else {
            orderService.orderMultiples(memberId);
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{memberId}/{orderId}")
    @PreAuthorize("@checker.isSelf(#memberId) or hasRole('ROLE_ADMIN')")
    public ResponseEntity cancelOrder(@PathVariable Long memberId, @PathVariable Long orderId) {
        orderService.cancel(orderId);
        return ResponseEntity.noContent().build();
    }


    /* FOR ADMIN */
    @GetMapping("/admin/{memberId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PagedModel<EntityModel<OrderResponse>>> getOrdersByAdminAndMemberId(
        @PathVariable Long memberId, @PageableDefault Pageable pageable) {

        List<OrderResponse> orderResponses = orderService.getListByMemberId(memberId);
        Page<OrderResponse> responsePage = PageUtil.toPage(orderResponses, pageable);
        return ResponseEntity.ok().body(assembler.toModel(responsePage));
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PagedModel<EntityModel<OrderResponse>>> getListBySearchConditionForAdmin(
        OrderSearchCondition condition, @PageableDefault Pageable pageable) {

        List<OrderResponse> orderResponses = orderService.getListBySearchCondition(condition);
        Page<OrderResponse> responsePage = PageUtil.toPage(orderResponses, pageable);
        return ResponseEntity.ok().body(assembler.toModel(responsePage));
    }
}

/*
URL 에 멤버 번호 노출되는게 좋지는 않은거 같은데 개선할 수 있을까?
memberId 로 Order 조회 -> 회원 & 관리자 나눌 필요 없이 권한 설정에서 본인만 + 관리자로 하면 될듯
 */