package com.woomoolmarket.controller.board.model;

import com.woomoolmarket.entity.board.entity.Board;
import com.woomoolmarket.controller.board.BoardController;
import com.woomoolmarket.service.board.dto.response.BoardResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class BoardModel extends EntityModel<Board> {
    public BoardModel(Board board, Link... links) {
        super(board, links);
        add(linkTo(BoardController.class).slash(board.getId()).withSelfRel());
    }

    public BoardModel(BoardResponse boardResponse, Link... links) {
        add(linkTo(BoardController.class).slash(boardResponse.getId()).withSelfRel());
    }
}
