package com.woomoolmarket.service.order.mapper;

import com.woomoolmarket.common.mapper.GenericMapper;
import com.woomoolmarket.domain.entity.Order;
import com.woomoolmarket.service.order.dto.request.OrderRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderRequestMapper extends GenericMapper<OrderRequest, Order> {

}
