package com.woomoolmarket.controller.cart;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.woomoolmarket.service.cart.CartService;
import com.woomoolmarket.service.cart.dto.response.CartResponse;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/carts", produces = MediaTypes.HAL_JSON_VALUE)
public class CartController {

    private final CartService cartService;
    private final PagedResourcesAssembler<CartResponse> assembler;

    @GetMapping("/{memberId}")
    public ResponseEntity<PagedModel<EntityModel<CartResponse>>> getListByMember(
        @PathVariable Long memberId, @PageableDefault Pageable pageable) {

        List<CartResponse> cartResponses = cartService.getListByMember(memberId);
        PageImpl<CartResponse> responsePage = new PageImpl<>(cartResponses, pageable, cartResponses.size());
        return ResponseEntity.ok(assembler.toModel(responsePage));
    }

    @PostMapping("/{memberId}")
    public ResponseEntity<Void> addToCart(@PathVariable Long memberId, Long productId, Integer quantity) {
        Long cartId = cartService.add(memberId, productId, quantity);
        URI createdUri = linkTo(methodOn(CartController.class).getOneById(memberId, cartId)).toUri();
        return ResponseEntity.created(createdUri).build();
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> removeAll(@PathVariable Long memberId) {
        cartService.removeAll(memberId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{memberId}/{cartId}")
    public ResponseEntity<EntityModel<CartResponse>> getOneById(@PathVariable Long memberId, @PathVariable Long cartId) {
        CartResponse cartResponse = cartService.getById(cartId);

        EntityModel<CartResponse> responseModel = EntityModel.of(
            cartResponse,
            linkTo(methodOn(CartController.class).getOneById(memberId, cartId)).withSelfRel(),
            linkTo(methodOn(CartController.class).getListByMember(memberId, Pageable.unpaged())).withRel("cart-list"));

        return ResponseEntity.ok().body(responseModel);
    }

    @DeleteMapping("/{memberId}/{cartId}")
    public ResponseEntity<Void> removeOne(@PathVariable Long memberId, @PathVariable Long cartId) {
        cartService.remove(cartId);
        return ResponseEntity.noContent().build();
    }
}
