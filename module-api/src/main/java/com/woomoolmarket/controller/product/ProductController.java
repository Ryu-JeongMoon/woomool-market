package com.woomoolmarket.controller.product;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.aop.time.LogExecutionTime;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.purchase.product.repository.ProductSearchCondition;
import com.woomoolmarket.service.product.ProductService;
import com.woomoolmarket.service.product.dto.request.ProductRequest;
import com.woomoolmarket.service.product.dto.request.ModifyProductRequest;
import com.woomoolmarket.service.product.dto.response.ProductResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        PageImpl<ProductResponse> responsePage = new PageImpl<>(productResponses, pageable, productResponses.size());
        return ResponseEntity.ok(assembler.toModel(responsePage));
    }

    @PostMapping
    public ResponseEntity create(@Validated @RequestBody ProductRequest createRequest, BindingResult bindingResult)
        throws JsonProcessingException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(bindingResult));
        }

        productService.create(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ProductResponse>> getById(@PathVariable Long id) {
        ProductResponse productResponse = productService.getByIdAndStatus(id, Status.ACTIVE);
        WebMvcLinkBuilder defaultLink = linkTo(methodOn(ProductController.class).getById(id));

        EntityModel<ProductResponse> productModel =
            EntityModel.of(
                productResponse,
                defaultLink.withSelfRel(),
                defaultLink.withRel("modify-product"),
                defaultLink.withRel("delete-product"));

        return ResponseEntity.ok(productModel);
    }

    @PatchMapping("/{id}")
    public ResponseEntity editProduct(@PathVariable Long id,
        @Validated @RequestBody ModifyProductRequest modifyRequest, BindingResult bindingResult) throws JsonProcessingException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(bindingResult));
        }

        ProductResponse productResponse = productService.edit(id, modifyRequest);
        EntityModel<ProductResponse> productModel =
            EntityModel.of(productResponse, linkTo(methodOn(ProductController.class).getById(id)).withSelfRel());

        return ResponseEntity.status(HttpStatus.CREATED).body(productModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteSoftly(id);
        return ResponseEntity.noContent().build();
    }


    /* FOR ADMIN */
    @GetMapping("/admin")
    public ResponseEntity<PagedModel<EntityModel<ProductResponse>>> getListBySearchConditionForAdmin(
        ProductSearchCondition condition, @PageableDefault Pageable pageable) {

        List<ProductResponse> productResponses = productService.getListBySearchConditionForAdmin(condition);
        PageImpl<ProductResponse> responsePage = new PageImpl<>(productResponses, pageable, productResponses.size());
        return ResponseEntity.ok(assembler.toModel(responsePage));
    }
}

/*
어드민을 위한 메서드의 경우 다른 Controller 에 작성하는 것이 좋을듯
 */