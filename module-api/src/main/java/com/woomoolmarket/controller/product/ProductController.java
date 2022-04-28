package com.woomoolmarket.controller.product;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.woomoolmarket.domain.enumeration.Status;
import com.woomoolmarket.domain.purchase.product.query.ProductQueryResponse;
import com.woomoolmarket.domain.purchase.product.repository.ProductSearchCondition;
import com.woomoolmarket.service.product.ProductService;
import com.woomoolmarket.service.product.dto.request.ProductModifyRequest;
import com.woomoolmarket.service.product.dto.request.ProductRequest;
import com.woomoolmarket.service.product.dto.response.ProductResponse;
import com.woomoolmarket.util.constant.ProductConstants;
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
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/products", produces = MediaTypes.HAL_JSON_VALUE)
public class ProductController {

  private final ProductService productService;
  private final PagedResourcesAssembler<ProductQueryResponse> queryAssembler;

  @GetMapping
  public ResponseEntity<PagedModel<EntityModel<ProductQueryResponse>>> getPageForMember(
    ProductSearchCondition condition, @PageableDefault Pageable pageable) {

    Page<ProductQueryResponse> queryResponsePage = productService.searchBy(condition, pageable);
    return ResponseEntity.ok(queryAssembler.toModel(queryResponsePage));
  }

  @PostMapping
  @PreAuthorize("hasAnyRole({'ROLE_SELLER', 'ROLE_ADMIN'})")
  public ResponseEntity<Void> create(@Valid @RequestBody ProductRequest productRequest) {
    productService.create(productRequest);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/{productId}")
  public ResponseEntity<EntityModel<ProductResponse>> getBy(@PathVariable Long productId) {
    ProductResponse productResponse = productService.findBy(productId, Status.ACTIVE);
    WebMvcLinkBuilder defaultLink = linkTo(methodOn(ProductController.class).getBy(productId));

    EntityModel<ProductResponse> productModel =
      EntityModel.of(
        productResponse,
        defaultLink.withSelfRel(),
        defaultLink.withRel(ProductConstants.MODIFY),
        defaultLink.withRel(ProductConstants.DELETE));

    return ResponseEntity.ok(productModel);
  }

  @PatchMapping("/{productId}")
  @PreAuthorize("@checker.isSelfByProductId(#productId) or hasRole('ROLE_ADMIN')")
  public ResponseEntity<Void> edit(@PathVariable Long productId, @Valid @RequestBody ProductModifyRequest modifyRequest) {
    productService.edit(productId, modifyRequest);
    URI uri = linkTo(methodOn(ProductController.class).getBy(productId)).withSelfRel().toUri();
    return ResponseEntity.created(uri).build();
  }

  @DeleteMapping("/{productId}")
  @PreAuthorize("@checker.isSelfByProductId(#productId) or hasRole('ROLE_ADMIN')")
  public ResponseEntity<Void> delete(@PathVariable Long productId) {
    productService.deleteSoftly(productId);
    return ResponseEntity.noContent().build();
  }


  /* FOR ADMIN */
  @GetMapping("/admin")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<PagedModel<EntityModel<ProductQueryResponse>>> getPageForAdmin(
    ProductSearchCondition condition, @PageableDefault Pageable pageable) {

    Page<ProductQueryResponse> queryResponsePage = productService.searchByAdmin(condition, pageable);
    return ResponseEntity.ok(queryAssembler.toModel(queryResponsePage));
  }
}