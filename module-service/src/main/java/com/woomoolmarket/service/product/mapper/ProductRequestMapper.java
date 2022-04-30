package com.woomoolmarket.service.product.mapper;

import com.woomoolmarket.common.mapper.GenericMapper;
import com.woomoolmarket.domain.entity.Product;
import com.woomoolmarket.service.product.dto.request.ProductRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductRequestMapper extends GenericMapper<ProductRequest, Product> {

  @Override
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(source = "stock", target = "stock", qualifiedByName = "atomicToInt",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  ProductRequest toDto(Product product);

  @Override
  Product toEntity(ProductRequest productRequest);

  @Override
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(source = "stock", target = "stock", qualifiedByName = "intToAtomic")
  void updateFromDto(ProductRequest dto, @MappingTarget Product entity);
}
