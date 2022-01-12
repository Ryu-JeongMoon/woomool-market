package com.woomoolmarket.controller.cart;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.woomoolmarket.domain.purchase.cart.query.CartQueryResponse;
import com.woomoolmarket.service.cart.CartService;
import com.woomoolmarket.service.cart.dto.request.CartIdRequest;
import com.woomoolmarket.service.cart.dto.request.CartRequest;
import com.woomoolmarket.service.cart.dto.response.CartResponse;
import com.woomoolmarket.util.constant.CartConstants;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
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
@RequestMapping(path = "/api/carts", produces = MediaTypes.HAL_JSON_VALUE)
public class CartController {

  private final CartService cartService;
  private final PagedResourcesAssembler<CartQueryResponse> queryAssembler;

  @GetMapping("/{memberId}/{cartId}")
  @PreAuthorize("@checker.isSelf(#memberId) or hasRole('ROLE_ADMIN')")
  public ResponseEntity<EntityModel<CartResponse>> getBy(@PathVariable Long memberId, @PathVariable Long cartId) {
    CartResponse cartResponse = cartService.findBy(cartId);

    EntityModel<CartResponse> responseModel = EntityModel.of(
      cartResponse,
      linkTo(methodOn(CartController.class).getBy(memberId, cartId)).withSelfRel(),
      linkTo(methodOn(CartController.class).getPageBy(memberId, Pageable.unpaged())).withRel(CartConstants.LIST));

    return ResponseEntity.ok().body(responseModel);
  }

  @GetMapping("/{memberId}")
  @PreAuthorize("@checker.isSelf(#memberId) or hasRole('ROLE_ADMIN')")
  public ResponseEntity<PagedModel<EntityModel<CartQueryResponse>>> getPageBy(
    @PathVariable Long memberId, @PageableDefault Pageable pageable) {

    Page<CartQueryResponse> cartQueryResponses = cartService.searchBy(memberId, pageable);
    return ResponseEntity.ok(queryAssembler.toModel(cartQueryResponses));
  }

  @PostMapping("/{memberId}")
  @PreAuthorize("@checker.isSelf(#memberId) or hasRole('ROLE_ADMIN')")
  public ResponseEntity<Void> addBy(@PathVariable Long memberId, @Valid @RequestBody CartRequest cartRequest) {
    Long cartId = cartService.add(cartRequest);
    URI createdUri = linkTo(methodOn(CartController.class).getBy(memberId, cartId)).toUri();
    return ResponseEntity.created(createdUri).build();
  }

  @DeleteMapping("/{memberId}")
  @PreAuthorize("@checker.isSelf(#memberId) or hasRole('ROLE_ADMIN')")
  public ResponseEntity<Void> removeAll(@PathVariable Long memberId) {
    cartService.removeAll(memberId);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{memberId}/{cartId}")
  @PreAuthorize("@checker.isSelf(#memberId) or hasRole('ROLE_ADMIN')")
  public ResponseEntity<Void> remove(@PathVariable Long memberId, @PathVariable Long cartId) {
    cartService.remove(cartId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{memberId}/picked")
  @PreAuthorize("@checker.isSelf(#memberId) or hasRole('ROLE_ADMIN')")
  public ResponseEntity<PagedModel<EntityModel<CartQueryResponse>>> getPickedBy(
    @PathVariable Long memberId, @RequestBody CartIdRequest cartIdRequest, @PageableDefault Pageable pageable) {

    Page<CartQueryResponse> queryResponsePage = cartService.findPickedBy(cartIdRequest.getCartIds(), pageable);
    return ResponseEntity.ok(queryAssembler.toModel(queryResponsePage));
  }


  /* FOR ADMIN */
  @GetMapping("/admin")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<PagedModel<EntityModel<CartQueryResponse>>> getPageForAdminBy(@PageableDefault Pageable pageable) {
    Page<CartQueryResponse> queryResponsePage = cartService.searchForAdminBy(pageable);
    return ResponseEntity.ok(queryAssembler.toModel(queryResponsePage));
  }
}