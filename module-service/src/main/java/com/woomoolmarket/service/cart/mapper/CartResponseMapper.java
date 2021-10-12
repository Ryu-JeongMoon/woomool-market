package com.woomoolmarket.service.cart.mapper;

import com.woomoolmarket.common.mapper.GenericMapper;
import com.woomoolmarket.domain.purchase.cart.entity.Cart;
import com.woomoolmarket.service.cart.dto.response.CartResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartResponseMapper extends GenericMapper<CartResponse, Cart> {

    @Override
    @Mappings({@Mapping(source = "member", target = "memberResponse"), @Mapping(source = "product", target = "productResponse")})
    CartResponse toDto(Cart cart);

    @Override
    @Mappings({@Mapping(source = "memberResponse", target = "member"), @Mapping(source = "productResponse", target = "product")})
    Cart toEntity(CartResponse cartResponse);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({@Mapping(source = "memberResponse", target = "member"), @Mapping(source = "productResponse", target = "product")})
    void updateFromDto(CartResponse dto, @MappingTarget Cart entity);
}
