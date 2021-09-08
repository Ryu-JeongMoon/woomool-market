package com.woomoolmarket.service.board.mapper;

import com.woomoolmarket.entity.board.entity.Board;
import com.woomoolmarket.common.mapper.GenericMapper;
import com.woomoolmarket.service.board.dto.request.RegisterBoardRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegisterBoardMapper extends GenericMapper<RegisterBoardRequest, Board> {}
