package com.woomoolmarket.service.product;

import com.woomoolmarket.util.constants.ExceptionConstants;
import com.woomoolmarket.domain.enumeration.Status;
import com.woomoolmarket.domain.image.entity.Image;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.query.ProductQueryResponse;
import com.woomoolmarket.domain.purchase.product.repository.ProductRepository;
import com.woomoolmarket.domain.purchase.product.repository.ProductSearchCondition;
import com.woomoolmarket.service.image.ImageProcessor;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ImageProcessor imageProcessor;
  private final ProductRepository productRepository;
  private final ProductRequestMapper productRequestMapper;
  private final ProductResponseMapper productResponseMapper;
  private final ProductModifyRequestMapper productModifyRequestMapper;

  @Transactional(readOnly = true)
  public ProductResponse findBy(Long id, Status status) {
    return productRepository.findByIdAndStatus(id, status)
      .map(productResponseMapper::toDto)
      .orElseThrow(() -> new EntityNotFoundException(ExceptionConstants.PRODUCT_NOT_FOUND));
  }

  @Transactional(readOnly = true)
  @Cacheable(keyGenerator = "customKeyGenerator", value = "products", unless = "#result == null")
  public Page<ProductQueryResponse> searchBy(ProductSearchCondition condition, Pageable pageable) {
    return productRepository.searchBy(condition, pageable);
  }

  @Transactional
  @Caching(evict = {
    @CacheEvict(keyGenerator = "customKeyGenerator", value = "products", allEntries = true),
    @CacheEvict(keyGenerator = "customKeyGenerator", value = "productsForAdmin", allEntries = true)})
  public void create(ProductRequest productRequest) {
    List<Image> images = imageProcessor.parse(productRequest.getMultipartFiles());
    Product product = productRequestMapper.toEntity(productRequest);
    product.addImages(images);
    productRepository.save(product);
  }

  @Transactional
  @Caching(evict = {
    @CacheEvict(keyGenerator = "customKeyGenerator", value = "products", allEntries = true),
    @CacheEvict(keyGenerator = "customKeyGenerator", value = "productsForAdmin", allEntries = true)})
  public void edit(Long id, ProductModifyRequest modifyRequest) {
    Product product = productRepository.findById(id)
      .orElseThrow(() -> new EntityNotFoundException(ExceptionConstants.PRODUCT_NOT_FOUND));

    List<Image> images = imageProcessor.parse(modifyRequest.getMultipartFiles());
    product.addImages(images);

    productModifyRequestMapper.updateFromDto(modifyRequest, product);
  }

  @Transactional
  @Caching(evict = {
    @CacheEvict(keyGenerator = "customKeyGenerator", value = "products", allEntries = true),
    @CacheEvict(keyGenerator = "customKeyGenerator", value = "productsForAdmin", allEntries = true)})
  public void deleteSoftly(Long id) {
    productRepository.findById(id)
      .orElseThrow(() -> new EntityNotFoundException(ExceptionConstants.PRODUCT_NOT_FOUND))
      .delete();
  }

  @Transactional
  @Caching(evict = {
    @CacheEvict(keyGenerator = "customKeyGenerator", value = "products", allEntries = true),
    @CacheEvict(keyGenerator = "customKeyGenerator", value = "productsForAdmin", allEntries = true)})
  public void restore(Long id) {
    productRepository.findById(id)
      .orElseThrow(() -> new EntityNotFoundException(ExceptionConstants.PRODUCT_NOT_FOUND))
      .restore();
  }

  /* FOR ADMIN */
  @Transactional(readOnly = true)
  @Cacheable(keyGenerator = "customKeyGenerator", value = "productsForAdmin", unless = "#result == null")
  public Page<ProductQueryResponse> searchByAdmin(ProductSearchCondition condition, Pageable pageable) {
    return productRepository.searchByAdmin(condition, pageable);
  }
}