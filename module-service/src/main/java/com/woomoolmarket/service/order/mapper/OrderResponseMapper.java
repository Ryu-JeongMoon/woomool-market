package com.woomoolmarket.service.order.mapper;

import com.woomoolmarket.common.mapper.GenericMapper;
import com.woomoolmarket.domain.purchase.order.entity.Order;
import com.woomoolmarket.service.order.dto.response.OrderResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderResponseMapper extends GenericMapper<OrderResponse, Order> {

}