package com.woomoolmarket.controller.cart;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.service.cart.CartService;
import com.woomoolmarket.service.cart.dto.request.CartRequest;
import com.woomoolmarket.service.cart.dto.response.CartResponse;
import com.woomoolmarket.util.PageUtil;
import java.net.URI;
import java.util.List;
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
import org.springframework.validation.BindingResult;
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

    private final ObjectMapper objectMapper;
    private final CartService cartService;
    private final PagedResourcesAssembler<CartResponse> assembler;

    @GetMapping("/{memberId}")
    @PreAuthorize("@checker.isSelf(#memberId) or hasRole('ROLE_ADMIN')")
    public ResponseEntity<PagedModel<EntityModel<CartResponse>>> getListByMember(
        @PathVariable Long memberId, @PageableDefault Pageable pageable) {

        List<CartResponse> cartResponses = cartService.getListByMember(memberId);
        Page<CartResponse> responsePage = PageUtil.toPage(cartResponses, pageable);
        return ResponseEntity.ok(assembler.toModel(responsePage));
    }

    @PostMapping("/{memberId}")
    @PreAuthorize("@checker.isSelf(#memberId) or hasRole('ROLE_ADMIN')")
    public ResponseEntity addToCart(@PathVariable Long memberId, @Valid @RequestBody CartRequest cartRequest,
        BindingResult bindingResult) throws JsonProcessingException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(bindingResult));
        }

        Long cartId = cartService.add(cartRequest);
        URI createdUri = linkTo(methodOn(CartController.class).getOneById(memberId, cartId)).toUri();
        return ResponseEntity.created(createdUri).build();
    }

    @DeleteMapping("/{memberId}")
    @PreAuthorize("@checker.isSelf(#memberId) or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> removeAll(@PathVariable Long memberId) {
        cartService.removeAllByMemberId(memberId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{memberId}/{cartId}")
    @PreAuthorize("@checker.isSelf(#memberId) or hasRole('ROLE_ADMIN')")
    public ResponseEntity<EntityModel<CartResponse>> getOneById(@PathVariable Long memberId, @PathVariable Long cartId) {
        CartResponse cartResponse = cartService.getById(cartId);

        EntityModel<CartResponse> responseModel = EntityModel.of(
            cartResponse,
            linkTo(methodOn(CartController.class).getOneById(memberId, cartId)).withSelfRel(),
            linkTo(methodOn(CartController.class).getListByMember(memberId, Pageable.unpaged())).withRel("cart-list"));

        return ResponseEntity.ok().body(responseModel);
    }

    @DeleteMapping("/{memberId}/{cartId}")
    @PreAuthorize("@checker.isSelf(#memberId) or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> removeOne(@PathVariable Long memberId, @PathVariable Long cartId) {
        cartService.removeByCartId(cartId);
        return ResponseEntity.noContent().build();
    }
}

/*
hasRole 로 Admin 일일이 정해주는게 중복이 너무 많다
admin-default 모든 메서드에 접근 가능하게 할 수 없을까?!
 */