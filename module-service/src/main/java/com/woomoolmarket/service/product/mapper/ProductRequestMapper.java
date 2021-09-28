package com.woomoolmarket.service.product.mapper;

import com.woomoolmarket.common.mapper.GenericMapper;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.service.product.dto.request.CreateProductRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductRequestMapper extends GenericMapper<CreateProductRequest, Product> {

}
