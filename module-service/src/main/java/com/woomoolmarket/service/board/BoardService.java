package com.woomoolmarket.service.board;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.common.util.ExceptionUtil;
import com.woomoolmarket.domain.board.entity.Board;
import com.woomoolmarket.domain.board.repository.BoardRepository;
import com.woomoolmarket.domain.board.repository.BoardSearchCondition;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.service.board.dto.request.BoardModifyRequest;
import com.woomoolmarket.service.board.dto.request.BoardRequest;
import com.woomoolmarket.service.board.dto.response.BoardResponse;
import com.woomoolmarket.service.board.mapper.BoardRequestMapper;
import com.woomoolmarket.service.board.mapper.BoardResponseMapper;
import com.woomoolmarket.service.board.mapper.BoardModifyMapper;
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
@Transactional
@RequiredArgsConstructor
public class BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final BoardResponseMapper boardResponseMapper;

    private final BoardRequestMapper boardRequestMapper;
    private final BoardModifyMapper modifyBoardRequestMapper;

    @Transactional(readOnly = true)
    @Cacheable(keyGenerator = "customKeyGenerator", value = "getListByCondition", unless = "#result==null")
    public List<BoardResponse> getListBySearchCondition(BoardSearchCondition searchCondition) {
        return boardRepository.findByCondition(searchCondition)
            .stream()
            .map(boardResponseMapper::toDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BoardResponse getByIdAndStatus(Long id, Status status) {
        return boardRepository.findByIdAndStatus(id, status)
            .map(boardResponseMapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.BOARD_NOT_FOUND));
    }

    public void increaseHit(Long id) {
        boardRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.BOARD_NOT_FOUND))
            .increaseHit();
    }

    // setMember 로 강제 주입 필요, 개선할 수 있을지?
    // 현재로서는 이메일로 찾아서 주입해주는 방법이 최선일 듯..
    @Caching(evict = {
        @CacheEvict(keyGenerator = "customKeyGenerator", value = "getListBySearchCondition", allEntries = true),
        @CacheEvict(keyGenerator = "customKeyGenerator", value = "getListByConditionForAdmin", allEntries = true)})
    public void register(BoardRequest boardRequest) {
        Board board = boardRequestMapper.toEntity(boardRequest);
        Member member = memberRepository.findByEmail(boardRequest.getEmail())
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.MEMBER_NOT_FOUND));
        board.setMember(member);
        boardRepository.save(board);
    }

    @Caching(evict = {
        @CacheEvict(keyGenerator = "customKeyGenerator", value = "getListBySearchCondition", allEntries = true),
        @CacheEvict(keyGenerator = "customKeyGenerator", value = "getListByConditionForAdmin", allEntries = true)})
    public BoardResponse edit(Long id, BoardModifyRequest modifyRequest) {
        Board board = boardRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.BOARD_NOT_FOUND));

        modifyBoardRequestMapper.updateFromDto(modifyRequest, board);
        return boardResponseMapper.toDto(board);
    }

    @Caching(evict = {
        @CacheEvict(keyGenerator = "customKeyGenerator", value = "getListBySearchCondition", allEntries = true),
        @CacheEvict(keyGenerator = "customKeyGenerator", value = "getListByConditionForAdmin", allEntries = true)})
    public void deleteSoftly(Long id) {
        boardRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.BOARD_NOT_FOUND))
            .delete();
    }

    @Caching(evict = {
        @CacheEvict(keyGenerator = "customKeyGenerator", value = "getListBySearchCondition", allEntries = true),
        @CacheEvict(keyGenerator = "customKeyGenerator", value = "getListByConditionForAdmin", allEntries = true)})
    public void restore(Long id) {
        boardRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.BOARD_NOT_FOUND))
            .restore();
    }


    /* FOR ADMIN */
    @Transactional(readOnly = true)
    @Cacheable(keyGenerator = "customKeyGenerator", value = "getListByConditionForAdmin", unless = "#result==null")
    public List<BoardResponse> getListBySearchConditionForAdmin(BoardSearchCondition condition) {
        return boardRepository.findByConditionForAdmin(condition)
            .stream()
            .map(boardResponseMapper::toDto)
            .collect(Collectors.toList());
    }
}

/*
하나의 로직 안에 Command, Query 겹쳐있지 않게 조회 로직에서 조회수 올리지 않고, 조회 후 increaseHit() 호출하도록 함
멱등성 지켜주기 위해 ?
-> delete, restore 호출할 때 Status 확인 후 로직 수행되게 함

@checker.isSelfByBoardId 체크 로직에서 Status.ACTIVE 만 걸러 받도록 했으니 중복 수행되지 않게 서비스 단에서는 체크 안 해줘도 됨
관리자를 위해서 중복으로 해줘야 하나?!
 */