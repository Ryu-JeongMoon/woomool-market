package com.woomoolmarket.controller.board;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.aop.time.LogExecutionTime;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.board.repository.BoardSearchCondition;
import com.woomoolmarket.service.board.BoardService;
import com.woomoolmarket.service.board.dto.request.BoardRequest;
import com.woomoolmarket.service.board.dto.request.ModifyBoardRequest;
import com.woomoolmarket.service.board.dto.response.BoardResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@LogExecutionTime
@RequiredArgsConstructor
@RequestMapping(path = "/api/boards", produces = MediaTypes.HAL_JSON_VALUE)
public class BoardController {

    private final BoardService boardService;
    private final ObjectMapper objectMapper;
    private final PagedResourcesAssembler<BoardResponse> assembler;

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<BoardResponse>>> getListBySearchConditionForMember(
        BoardSearchCondition condition, @PageableDefault Pageable pageable) {

        List<BoardResponse> boardResponses = boardService.getListBySearchCondition(condition);
        PageImpl<BoardResponse> responsePage = new PageImpl<>(boardResponses, pageable, boardResponses.size());
        return ResponseEntity.ok(assembler.toModel(responsePage));
    }

    @PostMapping
    public ResponseEntity registerBoard(@Validated BoardRequest registerBoardRequest, BindingResult bindingResult)
        throws JsonProcessingException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(bindingResult));
        }

        boardService.register(registerBoardRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<BoardResponse>> getActiveBoardById(@PathVariable Long id) {
        boardService.increaseHit(id);
        BoardResponse boardResponse = boardService.getByIdAndStatus(id, Status.ACTIVE);
        WebMvcLinkBuilder defaultLink = linkTo(methodOn(BoardController.class).getActiveBoardById(id));

        EntityModel<BoardResponse> responseModel =
            EntityModel.of(
                boardResponse,
                defaultLink.withSelfRel(),
                defaultLink.withRel("modify-board"),
                defaultLink.withRel("delete-board"));

        return ResponseEntity.ok(responseModel);
    }

    @PatchMapping("/{id}")
    public ResponseEntity editBoardInfo(@PathVariable Long id,
        @Validated @RequestBody ModifyBoardRequest modifyRequest, BindingResult bindingResult) throws JsonProcessingException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(bindingResult));
        }

        BoardResponse boardResponse = boardService.edit(id, modifyRequest);
        EntityModel<BoardResponse> boardModel =
            EntityModel.of(boardResponse, linkTo(methodOn(BoardController.class).getActiveBoardById(id)).withSelfRel());

        return ResponseEntity.status(HttpStatus.CREATED).body(boardModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        boardService.deleteSoftly(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/deleted/{id}")
    public ResponseEntity<Void> restoreBoard(@PathVariable Long id) {
        boardService.restore(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /* FOR ADMIN */
    @GetMapping("/admin")
    public ResponseEntity<PagedModel<EntityModel<BoardResponse>>> getListBySearchConditionForAdmin(
        BoardSearchCondition condition, @PageableDefault Pageable pageable) {

        List<BoardResponse> boardResponses = boardService.getListBySearchConditionForAdmin(condition);
        PageImpl<BoardResponse> responsePage = new PageImpl<>(boardResponses, pageable, boardResponses.size());
        return ResponseEntity.ok(assembler.toModel(responsePage));
    }
}

/*
게시글 복구의 경우 URI 어떻게 받을 것인지?
api/boards/deleted/{id} ??
 */