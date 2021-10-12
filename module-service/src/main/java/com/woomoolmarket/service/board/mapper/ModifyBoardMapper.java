package com.woomoolmarket.service.board.mapper;

import com.woomoolmarket.common.mapper.GenericMapper;
import com.woomoolmarket.domain.board.entity.Board;
import com.woomoolmarket.service.board.dto.request.ModifyBoardRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ModifyBoardMapper extends GenericMapper<ModifyBoardRequest, Board> {

}
