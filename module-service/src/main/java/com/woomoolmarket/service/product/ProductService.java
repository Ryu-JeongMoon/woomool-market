package com.woomoolmarket.service.product;

import static java.util.stream.Collectors.toList;

import com.woomoolmarket.common.constant.ExceptionConstant;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.repository.ProductRepository;
import com.woomoolmarket.domain.purchase.product.repository.ProductSearchCondition;
import com.woomoolmarket.service.product.dto.request.ProductModifyRequest;
import com.woomoolmarket.service.product.dto.request.ProductRequest;
import com.woomoolmarket.service.product.dto.response.ProductResponse;
import com.woomoolmarket.service.product.mapper.ProductModifyRequestMapper;
import com.woomoolmarket.service.product.mapper.ProductRequestMapper;
import com.woomoolmarket.service.product.mapper.ProductResponseMapper;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductRequestMapper productRequestMapper;
    private final ProductResponseMapper productResponseMapper;
    private final ProductModifyRequestMapper productModifyRequestMapper;

    @Transactional(readOnly = true)
    public ProductResponse getByIdAndStatus(Long id, Status status) {
        return productRepository.findByIdAndStatus(id, status)
            .map(productResponseMapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionConstant.PRODUCT_NOT_FOUND));
    }

    @Transactional(readOnly = true)
//    @Cacheable(keyGenerator = "customKeyGenerator", value = "products", unless = "#result == null", cacheManager = "cacheManager")
    public List<ProductResponse> getListBySearchConditionForMember(ProductSearchCondition searchCondition) {
        return productRepository.findByCondition(searchCondition)
            .stream()
            .map(productResponseMapper::toDto)
            .collect(toList());
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(keyGenerator = "customKeyGenerator", value = "products", allEntries = true),
        @CacheEvict(keyGenerator = "customKeyGenerator", value = "productsForAdmin", allEntries = true)})
    public void create(ProductRequest productRequest) {
        Product product = productRequestMapper.toEntity(productRequest);
        productRepository.save(product);
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(keyGenerator = "customKeyGenerator", value = "products", allEntries = true),
        @CacheEvict(keyGenerator = "customKeyGenerator", value = "productsForAdmin", allEntries = true)})
    public ProductResponse edit(Long id, ProductModifyRequest modifyRequest) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionConstant.PRODUCT_NOT_FOUND));
        productModifyRequestMapper.updateFromDto(modifyRequest, product);
        return productResponseMapper.toDto(product);
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(keyGenerator = "customKeyGenerator", value = "products", allEntries = true),
        @CacheEvict(keyGenerator = "customKeyGenerator", value = "productsForAdmin", allEntries = true)})
    public void deleteSoftly(Long id) {
        productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionConstant.PRODUCT_NOT_FOUND))
            .delete();
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(keyGenerator = "customKeyGenerator", value = "products", allEntries = true),
        @CacheEvict(keyGenerator = "customKeyGenerator", value = "productsForAdmin", allEntries = true)})
    public void restore(Long id) {
        productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionConstant.PRODUCT_NOT_FOUND))
            .restore();
    }

    /* FOR ADMIN */
    @Transactional(readOnly = true)
//    @Cacheable(keyGenerator = "customKeyGenerator", value = "productsForAdmin", unless = "#result == null", cacheManager = "cacheManager")
    public List<ProductResponse> getListBySearchConditionForAdmin(ProductSearchCondition searchCondition) {
        return productRepository.findByConditionForAdmin(searchCondition)
            .stream()
            .map(productResponseMapper::toDto)
            .collect(toList());
    }
}