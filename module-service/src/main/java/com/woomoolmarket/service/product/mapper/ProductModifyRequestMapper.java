package com.woomoolmarket.service.product.mapper;

import com.woomoolmarket.common.mapper.GenericMapper;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.service.product.dto.request.ProductModifyRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductModifyRequestMapper extends GenericMapper<ProductModifyRequest, Product> {

    @Override
    @Mapping(source = "stock", target = "stock", qualifiedByName = "atomicToInt")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProductModifyRequest toDto(Product product);

    @Override
    Product toEntity(ProductModifyRequest productModifyRequest);

    @Override
    @Mapping(source = "stock", target = "stock", qualifiedByName = "intToAtomic")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(ProductModifyRequest dto, @MappingTarget Product entity);
}
