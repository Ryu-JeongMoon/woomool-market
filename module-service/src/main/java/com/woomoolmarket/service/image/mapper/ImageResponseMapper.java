package com.woomoolmarket.service.image.mapper;

import com.woomoolmarket.common.mapper.GenericMapper;
import com.woomoolmarket.domain.entity.Image;
import com.woomoolmarket.service.image.dto.response.ImageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImageResponseMapper extends GenericMapper<ImageResponse, Image> {

}
