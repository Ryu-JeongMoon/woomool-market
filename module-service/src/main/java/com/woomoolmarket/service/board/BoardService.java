package com.woomoolmarket.service.board;

import static java.util.stream.Collectors.toList;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.board.entity.Board;
import com.woomoolmarket.domain.board.repository.BoardRepository;
import com.woomoolmarket.service.board.dto.request.ModifyBoardRequest;
import com.woomoolmarket.service.board.dto.request.RegisterBoardRequest;
import com.woomoolmarket.service.board.dto.response.BoardResponse;
import com.woomoolmarket.service.board.mapper.BoardResponseMapper;
import com.woomoolmarket.service.board.mapper.ModifyBoardMapper;
import com.woomoolmarket.service.board.mapper.RegisterBoardMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardResponseMapper boardResponseMapper;

    private final RegisterBoardMapper registerBoardMapper;
    private final ModifyBoardMapper modifyBoardRequestMapper;

    public List<BoardResponse> findAllBoards() {
        return boardRepository.findAll()
            .stream()
            .parallel()
            .map(boardResponseMapper::toDto)
            .collect(Collectors.toList());
    }

    public List<BoardResponse> findAllActiveBoards() {
        return boardRepository.findAll()
            .stream()
            .parallel()
            .filter(board -> board.getStatus() == Status.ACTIVE)
            .map(boardResponseMapper::toDto)
            .collect(Collectors.toList());
    }

    public List<BoardResponse> findAllInactiveBoards() {
        return boardRepository.findAll()
            .stream()
            .parallel()
            .filter(board -> board.getStatus() == Status.INACTIVE)
            .map(boardResponseMapper::toDto)
            .collect(Collectors.toList());
    }

    public BoardResponse findActiveBoard(Long id) {
        return boardResponseMapper.toDto(boardRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 글 번호입니다")));
    }

//    public List<BoardResponse> findActiveBoarByNickname(String nickname) {
//        return boardRepository.findAllByNickname(nickname)
//                              .stream()
//                              .map(boardResponseMapper::toDto)
//                              .collect(Collectors.toList());
//    }

    public List<BoardResponse> findActiveBoards() {
        return boardRepository.findAll()
            .stream()
            .filter(board -> board.getStatus() != Status.INACTIVE)
            .map(boardResponseMapper::toDto)
            .collect(toList());
    }

    @Transactional
    public void increaseHit(Long id) {
        boardRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 글 번호입니다"))
            .changeHit();
    }

    @Transactional
    public BoardResponse registerBoard(RegisterBoardRequest registerBoardRequest) {
        return boardResponseMapper.toDto(boardRepository.save(registerBoardMapper.toEntity(registerBoardRequest)));
    }

    @Transactional
    public void editBoard(Long id, ModifyBoardRequest modifyBoardRequest) {
        Board board = boardRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 글 번호입니다"));

        modifyBoardRequestMapper.updateFromDto(modifyBoardRequest, board);
    }

    @Transactional
    public void deleteSoftly(Long id) {
        boardRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("존재하지 않는 글 번호입니다"))
            .changeStatus(Status.INACTIVE, LocalDateTime.now());
    }
}
