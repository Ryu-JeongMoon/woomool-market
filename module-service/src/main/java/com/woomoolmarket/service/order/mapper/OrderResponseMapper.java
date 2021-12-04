package com.woomoolmarket.service.order.mapper;

import com.woomoolmarket.common.mapper.GenericMapper;
import com.woomoolmarket.domain.purchase.order.entity.Order;
import com.woomoolmarket.service.order.dto.response.OrderResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderResponseMapper extends GenericMapper<OrderResponse, Order> {

    @Override
    @Mappings(@Mapping(source = "member", target = "memberResponse"))
    OrderResponse toDto(Order order);

    @Override
    @Mappings(@Mapping(source = "memberResponse", target = "member"))
    Order toEntity(OrderResponse orderResponse);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings(@Mapping(source = "memberResponse", target = "member"))
    void updateFromDto(OrderResponse dto, @MappingTarget Order entity);
}
