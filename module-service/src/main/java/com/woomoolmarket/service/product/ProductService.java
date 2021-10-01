package com.woomoolmarket.service.product;

import static java.util.stream.Collectors.toList;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.common.util.ExceptionUtil;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.repository.ProductRepository;
import com.woomoolmarket.domain.purchase.product.repository.ProductSearchCondition;
import com.woomoolmarket.service.product.dto.request.CreateProductRequest;
import com.woomoolmarket.service.product.dto.request.ModifyProductRequest;
import com.woomoolmarket.service.product.dto.response.ProductResponse;
import com.woomoolmarket.service.product.mapper.ModifyProductRequestMapper;
import com.woomoolmarket.service.product.mapper.ProductRequestMapper;
import com.woomoolmarket.service.product.mapper.ProductResponseMapper;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(keyGenerator = "customKeyGenerator", value = "getList", unless = "#result==null")
    public List<ProductResponse> getList() {
        return productRepository.findAll()
            .stream()
            .map(productResponseMapper::toDto)
            .collect(toList());
    }

    @Cacheable(keyGenerator = "customKeyGenerator", value = "getListByStatus", unless = "#result==null")
    public List<ProductResponse> getListByStatus(Status status) {
        return productRepository.findByStatus(status)
            .stream()
            .map(productResponseMapper::toDto)
            .collect(Collectors.toList());
    }

    // 동적쿼리 캐싱은 어찌하남?
    public List<ProductResponse> getListBySearchCondition(ProductSearchCondition searchCondition) {
        return productRepository.findByCondition(searchCondition)
            .stream()
            .map(productResponseMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(keyGenerator = "customKeyGenerator", value = "getList", allEntries = true)
    public void create(CreateProductRequest productRequest) {
        Product product = productRequestMapper.toEntity(productRequest);
        productRepository.save(product);
    }

    @Transactional
    @CacheEvict(keyGenerator = "customKeyGenerator", value = "getList", allEntries = true)
    public ProductResponse edit(Long id, ModifyProductRequest modifyRequest) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.PRODUCT_NOT_FOUND));
        modifyProductRequestMapper.updateFromDto(modifyRequest, product);
        return productResponseMapper.toDto(product);
    }

    @Transactional
    @CacheEvict(keyGenerator = "customKeyGenerator", value = "getList", allEntries = true)
    public void deleteSoftly(Long id) {
        productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.PRODUCT_NOT_FOUND))
            .delete();
    }
}