package com.woomoolmarket.controller.product;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.aop.time.LogExecutionTime;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.purchase.product.repository.ProductSearchCondition;
import com.woomoolmarket.service.product.ProductService;
import com.woomoolmarket.service.product.dto.request.ProductModifyRequest;
import com.woomoolmarket.service.product.dto.request.ProductRequest;
import com.woomoolmarket.service.product.dto.response.ProductResponse;
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
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@LogExecutionTime
@RequiredArgsConstructor
@RequestMapping(path = "/api/products", produces = MediaTypes.HAL_JSON_VALUE)
public class ProductController {

    private final ObjectMapper objectMapper;
    private final ProductService productService;
    private final PagedResourcesAssembler<ProductResponse> assembler;

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ProductResponse>>> getListBySearchConditionForMember(
        ProductSearchCondition condition, @PageableDefault Pageable pageable) {

        List<ProductResponse> productResponses = productService.getListBySearchConditionForMember(condition);
        Page<ProductResponse> responsePage = PageUtil.toPage(productResponses, pageable);
        return ResponseEntity.ok(assembler.toModel(responsePage));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole({'ROLE_SELLER', 'ROLE_ADMIN'})")
    public ResponseEntity create(@Validated @RequestBody ProductRequest productRequest, BindingResult bindingResult)
        throws JsonProcessingException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(bindingResult));
        }

        productService.create(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<EntityModel<ProductResponse>> getById(@PathVariable Long productId) {
        ProductResponse productResponse = productService.getByIdAndStatus(productId, Status.ACTIVE);
        WebMvcLinkBuilder defaultLink = linkTo(methodOn(ProductController.class).getById(productId));

        EntityModel<ProductResponse> productModel =
            EntityModel.of(
                productResponse,
                defaultLink.withSelfRel(),
                defaultLink.withRel("modify-product"),
                defaultLink.withRel("delete-product"));

        return ResponseEntity.ok(productModel);
    }

    @PatchMapping("/{productId}")
    @PreAuthorize("@checker.isSelfByProductId(#productId) or hasRole('ROLE_ADMIN')")
    public ResponseEntity editProduct(@PathVariable Long productId,
        @Validated @RequestBody ProductModifyRequest modifyRequest, BindingResult bindingResult) throws JsonProcessingException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(bindingResult));
        }

        ProductResponse productResponse = productService.edit(productId, modifyRequest);
        EntityModel<ProductResponse> productModel =
            EntityModel.of(productResponse, linkTo(methodOn(ProductController.class).getById(productId)).withSelfRel());

        return ResponseEntity.status(HttpStatus.CREATED).body(productModel);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("@checker.isSelfByProductId(#productId) or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteSoftly(productId);
        return ResponseEntity.noContent().build();
    }


    /* FOR ADMIN */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PagedModel<EntityModel<ProductResponse>>> getListBySearchConditionForAdmin(
        ProductSearchCondition condition, @PageableDefault Pageable pageable) {

        List<ProductResponse> productResponses = productService.getListBySearchConditionForAdmin(condition);
        Page<ProductResponse> responsePage = PageUtil.toPage(productResponses, pageable);
        return ResponseEntity.ok(assembler.toModel(responsePage));
    }
}

/*
어드민을 위한 메서드의 경우 다른 Controller 에 작성하는 것이 좋을듯
 */