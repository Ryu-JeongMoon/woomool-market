package com.woomoolmarket.controller.board;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.woomoolmarket.domain.entity.enumeration.Status;
import com.woomoolmarket.domain.repository.querydto.BoardQueryResponse;
import com.woomoolmarket.domain.repository.querydto.BoardSearchCondition;
import com.woomoolmarket.service.board.BoardCountService;
import com.woomoolmarket.service.board.BoardService;
import com.woomoolmarket.service.board.dto.request.BoardModifyRequest;
import com.woomoolmarket.service.board.dto.request.BoardRequest;
import com.woomoolmarket.service.board.dto.response.BoardResponse;
import com.woomoolmarket.util.constant.BoardConstants;
import java.util.List;
import javax.validation.Valid;
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
@RequiredArgsConstructor
@RequestMapping(path = "/api/boards", produces = { MediaTypes.HAL_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE })
public class BoardController {

  private final BoardService boardService;
  private final BoardCountService boardCountService;
  private final PagedResourcesAssembler<BoardQueryResponse> queryAssembler;

  @GetMapping
  public ResponseEntity<PagedModel<EntityModel<BoardQueryResponse>>> getPageBy(
    BoardSearchCondition condition, @PageableDefault Pageable pageable) {

    Page<BoardQueryResponse> boardQueryResponses = boardService.searchBy(condition, pageable);
    return ResponseEntity.ok(queryAssembler.toModel(boardQueryResponses));
  }

  @GetMapping("/{id}")
  public ResponseEntity<EntityModel<BoardResponse>> getBy(@PathVariable Long id) {
    boardService.increaseHitByDB(id);
    BoardResponse boardResponse = boardService.findBy(id, Status.ACTIVE);
    WebMvcLinkBuilder defaultLink = linkTo(methodOn(BoardController.class).getBy(id));

    EntityModel<BoardResponse> responseModel =
      EntityModel.of(
        boardResponse,
        defaultLink.withSelfRel(),
        defaultLink.withRel(BoardConstants.MODIFY),
        defaultLink.withRel(BoardConstants.DELETE));

    return ResponseEntity.ok(responseModel);
  }

  @PostMapping
  @PreAuthorize("hasAnyRole({'ROLE_USER', 'ROLE_SELLER'}) and @checker.isQnaOrFree(#boardRequest) or hasRole('ROLE_ADMIN')")
  public ResponseEntity<Void> create(@Valid @RequestBody BoardRequest boardRequest, List<MultipartFile> files) {
    boardService.write(boardRequest, files);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PatchMapping("/{id}")
  @PreAuthorize("@checker.isSelfByBoardId(#id) or hasRole('ROLE_ADMIN')")
  public ResponseEntity<EntityModel<BoardResponse>> edit(
    @PathVariable Long id, @Valid @RequestBody BoardModifyRequest modifyRequest) {

    BoardResponse boardResponse = boardService.edit(id, modifyRequest);
    EntityModel<BoardResponse> boardModel =
      EntityModel.of(boardResponse, linkTo(methodOn(BoardController.class).getBy(id)).withSelfRel());

    return ResponseEntity.status(HttpStatus.CREATED).body(boardModel);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("@checker.isSelfByBoardId(#id) or hasRole('ROLE_ADMIN')")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    boardService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/deleted/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<Void> restore(@PathVariable Long id) {
    boardService.restore(id);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }


  /* FOR ADMIN */
  @GetMapping("/admin")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<PagedModel<EntityModel<BoardQueryResponse>>> getPageForAdminBy(
    BoardSearchCondition condition, @PageableDefault Pageable pageable) {

    Page<BoardQueryResponse> queryResponsePage = boardService.searchForAdminBy(condition, pageable);
    return ResponseEntity.ok(queryAssembler.toModel(queryResponsePage));
  }
}