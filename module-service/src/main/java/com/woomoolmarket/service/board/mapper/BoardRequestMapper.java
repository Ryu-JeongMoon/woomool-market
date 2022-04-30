package com.woomoolmarket.service.board.mapper;

import com.woomoolmarket.common.mapper.GenericMapper;
import com.woomoolmarket.domain.entity.Board;
import com.woomoolmarket.service.board.dto.request.BoardRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BoardRequestMapper extends GenericMapper<BoardRequest, Board> {

}
