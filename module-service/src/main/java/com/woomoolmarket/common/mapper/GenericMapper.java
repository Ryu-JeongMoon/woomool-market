package com.woomoolmarket.common.mapper;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

public interface GenericMapper<D, E> {

  @Named(value = "atomicToInt")
  default Integer atomicToInt(AtomicInteger stock) {
    return stock.intValue();
  }

  @Named(value = "intToAtomic")
  default AtomicInteger intToAtomic(Integer stock) {
    return new AtomicInteger(stock);
  }

  D toDto(E e);

  E toEntity(D d);

  List<D> toDtoList(List<E> entityList);

  List<E> toEntityList(List<D> dtoList);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateFromDto(D dto, @MappingTarget E entity);
}
