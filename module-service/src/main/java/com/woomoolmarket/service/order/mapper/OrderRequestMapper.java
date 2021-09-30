package com.woomoolmarket.service.order.mapper;

import com.woomoolmarket.common.mapper.GenericMapper;
import com.woomoolmarket.domain.purchase.order.entity.Order;
import com.woomoolmarket.service.order.dto.request.CreateOrderRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderRequestMapper extends GenericMapper<CreateOrderRequest, Order> {

}
