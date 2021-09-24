package com.woomoolmarket.service.order.mapper;

import com.woomoolmarket.common.mapper.GenericMapper;
import com.woomoolmarket.domain.purchase.order.entity.Order;
import com.woomoolmarket.service.order.dto.request.ModifyOrderRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ModifyOrderRequestMapper extends GenericMapper<ModifyOrderRequest, Order> {

}
