package com.woomoolmarket.service.product.mapper;

import com.woomoolmarket.common.mapper.GenericMapper;
import com.woomoolmarket.domain.entity.Product;
import com.woomoolmarket.service.product.dto.response.ProductResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductResponseMapper extends GenericMapper<ProductResponse, Product> {

  @Override
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mappings({
    @Mapping(source = "member", target = "memberResponse"),
    @Mapping(source = "stock", target = "stock", qualifiedByName = "atomicToInt")})
  ProductResponse toDto(Product product);

  @Override
  @Mapping(source = "memberResponse", target = "member")
  Product toEntity(ProductResponse productResponse);

  @Override
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mappings({
    @Mapping(source = "memberResponse", target = "member"),
    @Mapping(source = "stock", target = "stock", qualifiedByName = "intToAtomic")})
  void updateFromDto(ProductResponse dto, @MappingTarget Product entity);
}
