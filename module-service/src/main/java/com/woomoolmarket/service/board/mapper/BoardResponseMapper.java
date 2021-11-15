package com.woomoolmarket.service.board.mapper;

import com.woomoolmarket.common.mapper.GenericMapper;
import com.woomoolmarket.domain.board.entity.Board;
import com.woomoolmarket.domain.board.dto.response.BoardResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BoardResponseMapper extends GenericMapper<BoardResponse, Board> {

    @Override
    @Mappings({@Mapping(source = "member", target = "memberResponse")})
    BoardResponse toDto(Board board);

    @Override
    @Mappings({@Mapping(source = "memberResponse", target = "member")})
    Board toEntity(BoardResponse boardResponse);

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({@Mapping(source = "memberResponse", target = "member")})
    void updateFromDto(BoardResponse dto, @MappingTarget Board entity);
}
