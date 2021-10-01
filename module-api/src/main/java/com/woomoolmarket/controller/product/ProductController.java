package com.woomoolmarket.controller.product;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.aop.time.LogExecutionTime;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.service.product.ProductService;
import com.woomoolmarket.service.product.dto.request.CreateProductRequest;
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
    public ResponseEntity<PagedModel<EntityModel<ProductResponse>>> getList(@PageableDefault Pageable pageable) {
        List<ProductResponse> response = productService.getListByStatus(Status.ACTIVE);
        PageImpl<ProductResponse> cacheResponse = new PageImpl<>(response, pageable, response.size());

        return ResponseEntity.ok(assembler.toModel(cacheResponse));
    }

    @PostMapping
    public ResponseEntity create(@Validated @RequestBody CreateProductRequest createRequest, BindingResult bindingResult)
        throws JsonProcessingException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(bindingResult));
        }

        productService.create(createRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ProductResponse>> get(@PathVariable Long id) {
        ProductResponse productResponse = productService.findProductById(id);
        WebMvcLinkBuilder defaultLink = linkTo(methodOn(ProductController.class).get(id));

        EntityModel<ProductResponse> responseModel =
            EntityModel.of(
                productResponse,
                defaultLink.withSelfRel(),
                defaultLink.withRel("modify-product"),
                defaultLink.withRel("delete-product"));

        return ResponseEntity.ok(responseModel);
    }

    @PatchMapping("/{id}")
    public ResponseEntity edit(@PathVariable Long id,
        @Validated @RequestBody ModifyProductRequest modifyRequest, BindingResult bindingResult) throws JsonProcessingException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(bindingResult));
        }

        ProductResponse productResponse = productService.edit(id, modifyRequest);
        EntityModel<ProductResponse> responseModel =
            EntityModel.of(productResponse, linkTo(methodOn(ProductController.class).get(id)).withSelfRel());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        productService.deleteSoftly(id);
        return ResponseEntity.noContent().build();
    }


    /* FOR ADMIN */
    @GetMapping("/admin/all")
    public ResponseEntity<PagedModel<EntityModel<ProductResponse>>> getListByAdmin(@PageableDefault Pageable pageable) {
        List<ProductResponse> responses = productService.getList();
        PageImpl<ProductResponse> cacheResponse = new PageImpl<>(responses, pageable, responses.size());

        return ResponseEntity.ok(assembler.toModel(cacheResponse));
    }

    @GetMapping("/admin/active")
    public ResponseEntity<PagedModel<EntityModel<ProductResponse>>> getActiveListByAdmin(@PageableDefault Pageable pageable) {
        List<ProductResponse> responses = productService.getListByStatus(Status.ACTIVE);
        PageImpl<ProductResponse> cacheResponse = new PageImpl<>(responses, pageable, responses.size());

        return ResponseEntity.ok(assembler.toModel(cacheResponse));
    }

    @GetMapping("/admin/inactive")
    public ResponseEntity<PagedModel<EntityModel<ProductResponse>>> getInactiveListByAdmin(@PageableDefault Pageable pageable) {
        List<ProductResponse> responses = productService.getListByStatus(Status.INACTIVE);
        PageImpl<ProductResponse> cacheResponse = new PageImpl<>(responses, pageable, responses.size());

        return ResponseEntity.ok(assembler.toModel(cacheResponse));
    }
}

/*
controller 계층 메서드 이름... 어떻게 지어야 잘 지은걸까?!
 */