package com.woomoolmarket.service.product;

import static java.util.stream.Collectors.toList;
import static org.springframework.beans.support.PagedListHolder.DEFAULT_MAX_LINKED_PAGES;
import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

import com.woomoolmarket.common.enumeration.Region;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.common.util.ExceptionUtil;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.entity.ProductCategory;
import com.woomoolmarket.domain.purchase.product.repository.ProductRepository;
import com.woomoolmarket.service.product.dto.request.ModifyProductRequest;
import com.woomoolmarket.service.product.dto.request.CreateProductRequest;
import com.woomoolmarket.service.product.dto.response.ProductResponse;
import com.woomoolmarket.service.product.mapper.ModifyProductRequestMapper;
import com.woomoolmarket.service.product.mapper.ProductRequestMapper;
import com.woomoolmarket.service.product.mapper.ProductResponseMapper;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductRequestMapper productRequestMapper;
    private final ProductResponseMapper productResponseMapper;
    private final ModifyProductRequestMapper modifyProductRequestMapper;

    public ProductResponse findProductById(Long id) {
        return productRepository.findById(id)
            .map(productResponseMapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.PRODUCT_NOT_FOUND));
    }

    public Page<ProductResponse> findAllProducts(
        @PageableDefault(page = DEFAULT_MAX_LINKED_PAGES, size = DEFAULT_PAGE_SIZE) Pageable pageRequest) {
        return new PageImpl<>(productRepository.findAll(pageRequest)
            .stream()
            .map(productResponseMapper::toDto)
            .collect(toList()));
    }

    // 조건과 메서드만 다르고 데이터 가공 방식은 같은데 중복을 없앨 방법이 있을까?
    // 동적 쿼리로 해결?!
    private Page<ProductResponse> findProductsByCondition(Page<Product> productPage) {
        return new PageImpl<>(productPage
            .stream()
            .map(productResponseMapper::toDto)
            .collect(Collectors.toList()));
    }

    public Page<ProductResponse> findProductsByName(String name,
        @PageableDefault(page = DEFAULT_MAX_LINKED_PAGES, size = DEFAULT_PAGE_SIZE) Pageable pageRequest) {
        return findProductsByCondition(productRepository.findProductsByName(name, pageRequest));
    }

    public Page<ProductResponse> findProductsBySeller(String seller,
        @PageableDefault(page = DEFAULT_MAX_LINKED_PAGES, size = DEFAULT_PAGE_SIZE) Pageable pageRequest) {
        return findProductsByCondition(productRepository.findProductsBySeller(seller, pageRequest));
    }

    public Page<ProductResponse> findProductsByRegion(Region region,
        @PageableDefault(page = DEFAULT_MAX_LINKED_PAGES, size = DEFAULT_PAGE_SIZE) Pageable pageRequest) {
        return findProductsByCondition(productRepository.findProductsByRegion(region, pageRequest));
    }

    public Page<ProductResponse> findProductsByProductCategory(ProductCategory productCategory,
        @PageableDefault(page = DEFAULT_MAX_LINKED_PAGES, size = DEFAULT_PAGE_SIZE) Pageable pageRequest) {
        return findProductsByCondition(productRepository.findProductsByProductCategory(productCategory, pageRequest));
    }

    public Page<ProductResponse> findProductsByPriceRange(int minPrice, int maxPrice,
        @PageableDefault(page = DEFAULT_MAX_LINKED_PAGES, size = DEFAULT_PAGE_SIZE) Pageable pageRequest) {
        return findProductsByCondition(productRepository.findProductsByPriceRange(minPrice, maxPrice, pageRequest));
    }

    public Page<ProductResponse> findProductsByStatus(Status status,
        @PageableDefault(page = DEFAULT_MAX_LINKED_PAGES, size = DEFAULT_PAGE_SIZE) Pageable pageRequest) {
        return findProductsByCondition(productRepository.findProductsByStatus(status, pageRequest));
    }

    // MemberService처럼 response body에 상품 정보를 담아보내 주는 것이 나을지
    // 단순히 created 상태 코드만 주는게 나을지 고민해보자
    @Transactional
    public Long createProduct(CreateProductRequest productRequest) {
        Product product = productRequestMapper.toEntity(productRequest);
        return productRepository.save(product).getId();
    }

    @Transactional
    public ProductResponse editProductInfo(Long id, ModifyProductRequest modifyRequest) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.PRODUCT_NOT_FOUND));
        modifyProductRequestMapper.updateFromDto(modifyRequest, product);
        return productResponseMapper.toDto(product);
    }

    /* 사용자 요청은 soft delete 하고 진짜 삭제는 batch job 으로 돌리자 batch 기준은 탈퇴 후 6개월? */
    @Transactional
    public void deleteSoftly(Long id) {
        productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.PRODUCT_NOT_FOUND))
            .delete();
    }
}
