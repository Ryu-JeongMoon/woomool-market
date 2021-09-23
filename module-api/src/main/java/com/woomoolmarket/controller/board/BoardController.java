package com.woomoolmarket.controller.board;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import com.woomoolmarket.aop.time.LogExecutionTime;
import com.woomoolmarket.controller.board.model.BoardModel;
import com.woomoolmarket.service.board.BoardService;
import com.woomoolmarket.service.board.dto.request.RegisterBoardRequest;
import com.woomoolmarket.service.board.dto.response.BoardResponse;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@LogExecutionTime
@RequestMapping(path = "/api/boards",
    produces = MediaTypes.HAL_JSON_VALUE)
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ResponseEntity getBoards() {
        return ResponseEntity.ok(boardService.findActiveBoards());
    }

    @PostMapping
    public ResponseEntity registerBoard(@Validated @RequestBody RegisterBoardRequest registerBoardRequest,
        BindingResult bindingResult) {
        /* TODO error 잡히면 error response 보내줍시당 */
        if (bindingResult.hasErrors()) {
            return null;
        }

        BoardResponse boardResponse = boardService.registerBoard(registerBoardRequest);

        WebMvcLinkBuilder builder = linkTo(BoardController.class).slash(boardResponse.getId());
        URI createdUri = builder.toUri();
        BoardModel boardModel = new BoardModel(boardResponse);

        return ResponseEntity.created(createdUri)
            .body(boardModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity getBoard(@PathVariable Long id) {
        boardService.increaseHit(id);
        BoardResponse boardResponse = boardService.findActiveBoard(id);

        return ResponseEntity.ok(boardResponse);
    }

//    @GetMapping("/{nickname}")
//    public ResponseEntity getBoardByNickname(@PathVariable String nickname) {
//        return ResponseEntity.ok(boardService.findActiveBoarByNickname(nickname));
//    }


    /* FOR ADMIN */
    @GetMapping("/admin/all")
    public ResponseEntity getAllBoards() {
        return ResponseEntity.ok(boardService.findAllBoards());
    }

    @GetMapping("/admin/active")
    public ResponseEntity getAllActiveBoards() {
        return ResponseEntity.ok(boardService.findAllActiveBoards());
    }

    @GetMapping("/admin/inactive")
    public ResponseEntity getAllInactiveBoards() {
        return ResponseEntity.ok(boardService.findAllInactiveBoards());
    }
}
