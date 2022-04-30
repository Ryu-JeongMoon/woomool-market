package com.woomoolmarket.controller.order;


import com.woomoolmarket.domain.repository.querydto.OrderQueryResponse;
import com.woomoolmarket.domain.repository.querydto.OrderSearchCondition;
import com.woomoolmarket.service.order.OrderService;
import com.woomoolmarket.service.order.dto.request.OrderDeleteRequest;
import com.woomoolmarket.service.order.dto.request.OrderRequest;
import javax.validation.Valid;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/orders", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
public class OrderController {

  private final OrderService orderService;
  private final PagedResourcesAssembler<OrderQueryResponse> queryAssembler;

  @GetMapping("/{memberId}")
  @PreAuthorize("@checker.isSelf(#memberId) or hasRole('ROLE_ADMIN')")
  public ResponseEntity<PagedModel<EntityModel<OrderQueryResponse>>> getPageBy(
    @PathVariable Long memberId, @PageableDefault Pageable pageable) {

    Page<OrderQueryResponse> orderQueryResponsePage = orderService.searchBy(memberId, pageable);
    return ResponseEntity.ok(queryAssembler.toModel(orderQueryResponsePage));
  }

  @PostMapping
  @PreAuthorize("@checker.isSelf(#orderRequest.memberId)")
  public ResponseEntity<Void> create(@Valid @RequestBody OrderRequest orderRequest) {
    orderService.order(orderRequest);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @DeleteMapping("/{memberId}")
  @PreAuthorize("@checker.isSelf(#deleteRequest.memberId) or hasRole('ROLE_ADMIN')")
  public ResponseEntity<Void> cancel(@Valid @RequestBody OrderDeleteRequest deleteRequest) {
    orderService.cancel(deleteRequest.getOrderId());
    return ResponseEntity.noContent().build();
  }


  /* FOR ADMIN */
  @GetMapping("/admin")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<PagedModel<EntityModel<OrderQueryResponse>>> getPageForAdminBy(
    OrderSearchCondition condition, @PageableDefault Pageable pageable) {

    Page<OrderQueryResponse> orderQueryResponses = orderService.searchForAdminBy(condition, pageable);
    return ResponseEntity.ok().body(queryAssembler.toModel(orderQueryResponses));
  }
}