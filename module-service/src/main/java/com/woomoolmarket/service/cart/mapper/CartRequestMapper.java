package com.woomoolmarket.service.cart.mapper;

import com.woomoolmarket.common.mapper.GenericMapper;
import com.woomoolmarket.domain.purchase.cart.entity.Cart;
import com.woomoolmarket.service.cart.dto.request.CartRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartRequestMapper extends GenericMapper<CartRequest, Cart> {

}
