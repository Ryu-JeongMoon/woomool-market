package com.woomoolmarket.service.board;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.common.util.ExceptionUtil;
import com.woomoolmarket.domain.board.entity.Board;
import com.woomoolmarket.domain.board.repository.BoardRepository;
import com.woomoolmarket.domain.board.repository.BoardSearchCondition;
import com.woomoolmarket.service.board.dto.request.BoardRequest;
import com.woomoolmarket.service.board.dto.request.ModifyBoardRequest;
import com.woomoolmarket.service.board.dto.response.BoardResponse;
import com.woomoolmarket.service.board.mapper.BoardResponseMapper;
import com.woomoolmarket.service.board.mapper.ModifyBoardMapper;
import com.woomoolmarket.service.board.mapper.RegisterBoardMapper;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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

    @Cacheable(keyGenerator = "customKeyGenerator", value = "getListByCondition", unless = "#result==null")
    public List<BoardResponse> getListBySearchCondition(BoardSearchCondition searchCondition) {
        return boardRepository.findByCondition(searchCondition)
            .stream()
            .map(boardResponseMapper::toDto)
            .collect(Collectors.toList());
    }

    public BoardResponse getByIdAndStatus(Long id, Status status) {
        return boardResponseMapper.toDto(boardRepository.findByIdAndStatus(id, status)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.BOARD_NOT_FOUND)));
    }

    @Transactional
    public void increaseHit(Long id) {
        boardRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.BOARD_NOT_FOUND))
            .changeHit();
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(keyGenerator = "customKeyGenerator", value = "getListBySearchCondition", allEntries = true),
        @CacheEvict(keyGenerator = "customKeyGenerator", value = "getListByConditionForAdmin", allEntries = true)})
    public void register(BoardRequest boardRequest) {
        Board board = registerBoardMapper.toEntity(boardRequest);
        boardRepository.save(board);
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(keyGenerator = "customKeyGenerator", value = "getListBySearchCondition", allEntries = true),
        @CacheEvict(keyGenerator = "customKeyGenerator", value = "getListByConditionForAdmin", allEntries = true)})
    public BoardResponse edit(Long id, ModifyBoardRequest modifyRequest) {
        Board board = boardRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.BOARD_NOT_FOUND));

        modifyBoardRequestMapper.updateFromDto(modifyRequest, board);
        return boardResponseMapper.toDto(board);
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(keyGenerator = "customKeyGenerator", value = "getListBySearchCondition", allEntries = true),
        @CacheEvict(keyGenerator = "customKeyGenerator", value = "getListByConditionForAdmin", allEntries = true)})
    public void deleteSoftly(Long id) {
        boardRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.BOARD_NOT_FOUND))
            .delete();
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(keyGenerator = "customKeyGenerator", value = "getListBySearchCondition", allEntries = true),
        @CacheEvict(keyGenerator = "customKeyGenerator", value = "getListByConditionForAdmin", allEntries = true)})
    public void restore(Long id) {
        boardRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.BOARD_NOT_FOUND))
            .restore();
    }


    /* FOR ADMIN */
    @Cacheable(keyGenerator = "customKeyGenerator", value = "getListByConditionForAdmin", unless = "#result==null")
    public List<BoardResponse> getListBySearchConditionForAdmin(BoardSearchCondition condition) {
        return boardRepository.findByConditionForAdmin(condition)
            .stream()
            .map(boardResponseMapper::toDto)
            .collect(Collectors.toList());
    }
}