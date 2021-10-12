package com.woomoolmarket.service.order.mapper;

import com.woomoolmarket.common.mapper.GenericMapper;
import com.woomoolmarket.domain.purchase.order.entity.Order;
import com.woomoolmarket.service.order.dto.response.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderResponseMapper extends GenericMapper<OrderResponse, Order> {

}
