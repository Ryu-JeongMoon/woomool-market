package com.woomoolmarket.service.board.mapper;

import com.woomoolmarket.common.mapper.GenericMapper;
import com.woomoolmarket.domain.board.entity.Board;
import com.woomoolmarket.service.board.dto.request.BoardRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegisterBoardMapper extends GenericMapper<BoardRequest, Board> {

}
