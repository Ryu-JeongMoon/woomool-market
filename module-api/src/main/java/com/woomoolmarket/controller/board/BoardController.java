package com.woomoolmarket.controller.board;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woomoolmarket.aop.annotation.LogExecutionTime;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.board.query.BoardQueryResponse;
import com.woomoolmarket.domain.board.repository.BoardSearchCondition;
import com.woomoolmarket.service.board.BoardService;
import com.woomoolmarket.service.board.dto.request.BoardModifyRequest;
import com.woomoolmarket.service.board.dto.request.BoardRequest;
import com.woomoolmarket.service.board.dto.response.BoardResponse;
import com.woomoolmarket.util.PageUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import org.springframework.web.multipart.MultipartFile;

@RestController
@LogExecutionTime
@RequiredArgsConstructor
@RequestMapping(path = "/api/boards", produces = {MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
public class BoardController {

    private final BoardService boardService;
    private final ObjectMapper objectMapper;
    private final PagedResourcesAssembler<BoardResponse> assembler;
    private final PagedResourcesAssembler<BoardQueryResponse> queryAssembler;

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<BoardQueryResponse>>> getBoardList(
        BoardSearchCondition condition, @PageableDefault Pageable pageable) {

        Page<BoardQueryResponse> boardQueryResponses = boardService.findListBySearchCondition(condition, pageable);
        return ResponseEntity.ok(queryAssembler.toModel(boardQueryResponses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<BoardResponse>> getBoard(@PathVariable Long id) {
        boardService.increaseHit(id);
        BoardResponse boardResponse = boardService.findByIdAndStatus(id, Status.ACTIVE);
        WebMvcLinkBuilder defaultLink = linkTo(methodOn(BoardController.class).getBoard(id));

        EntityModel<BoardResponse> responseModel =
            EntityModel.of(
                boardResponse,
                defaultLink.withSelfRel(),
                defaultLink.withRel("modify-board"),
                defaultLink.withRel("delete-board"));

        return ResponseEntity.ok(responseModel);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_SELLER'}) and @checker.isQnaOrFree(#boardRequest) or hasRole('ROLE_ADMIN')")
    public ResponseEntity createBoard(@Validated @RequestBody BoardRequest boardRequest, BindingResult bindingResult,
        List<MultipartFile> files) throws JsonProcessingException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(bindingResult));
        }

        boardService.register(boardRequest, files);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@checker.isSelfByBoardId(#id) or hasRole('ROLE_ADMIN')")
    public ResponseEntity editBoard(@PathVariable Long id,
        @Validated @RequestBody BoardModifyRequest modifyRequest, BindingResult bindingResult) throws JsonProcessingException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(objectMapper.writeValueAsString(bindingResult));
        }

        BoardResponse boardResponse = boardService.edit(id, modifyRequest);
        EntityModel<BoardResponse> boardModel =
            EntityModel.of(boardResponse, linkTo(methodOn(BoardController.class).getBoard(id)).withSelfRel());

        return ResponseEntity.status(HttpStatus.CREATED).body(boardModel);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@checker.isSelfByBoardId(#id) or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        boardService.deleteSoftly(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/deleted/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> restoreBoard(@PathVariable Long id) {
        boardService.restore(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /* FOR ADMIN */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PagedModel<EntityModel<BoardResponse>>> getBoardListForAdmin(
        BoardSearchCondition condition, @PageableDefault Pageable pageable) {

        List<BoardResponse> boardResponses = boardService.findListBySearchConditionForAdmin(condition);
        Page<BoardResponse> responsePage = PageUtil.toPage(boardResponses, pageable);
        return ResponseEntity.ok(assembler.toModel(responsePage));
    }
}