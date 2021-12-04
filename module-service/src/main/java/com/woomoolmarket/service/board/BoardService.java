package com.woomoolmarket.service.board;

import com.woomoolmarket.common.constant.ExceptionConstant;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.board.entity.Board;
import com.woomoolmarket.domain.board.query.BoardQueryResponse;
import com.woomoolmarket.domain.board.repository.BoardRepository;
import com.woomoolmarket.domain.board.repository.BoardSearchCondition;
import com.woomoolmarket.domain.image.entity.Image;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.service.board.dto.request.BoardModifyRequest;
import com.woomoolmarket.service.board.dto.request.BoardRequest;
import com.woomoolmarket.service.board.dto.response.BoardResponse;
import com.woomoolmarket.service.board.mapper.BoardModifyMapper;
import com.woomoolmarket.service.board.mapper.BoardRequestMapper;
import com.woomoolmarket.service.board.mapper.BoardResponseMapper;
import com.woomoolmarket.service.image.ImageProcessor;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final ImageProcessor imageProcessor;
    private final MemberRepository memberRepository;

    private final BoardRepository boardRepository;
    private final BoardModifyMapper boardModifyMapper;
    private final BoardRequestMapper boardRequestMapper;
    private final BoardResponseMapper boardResponseMapper;

    @Transactional(readOnly = true)
//    @Cacheable(keyGenerator = "customKeyGenerator", value = "boards", cacheManager = "jdkCacheManager")
    public Page<BoardQueryResponse> findListBySearchCondition(BoardSearchCondition searchCondition, Pageable pageable) {
        return boardRepository.findByConditionAndPage(searchCondition, pageable);
    }

    @Transactional(readOnly = true)
//    @Cacheable(keyGenerator = "customKeyGenerator", value = "panda", cacheManager = "jsonCacheManager")
    public BoardResponse findByIdAndStatus(Long id, Status status) {
        return boardRepository.findByIdAndStatus(id, status)
            .map(boardResponseMapper::toDto)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionConstant.BOARD_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<BoardResponse> findByMemberAndStatus(String email, Status status) {
        List<Board> boards = boardRepository.findByMemberAndStatus(email, status);
        return boardResponseMapper.toDtoList(boards);

    }

    @Transactional
    public void increaseHit(Long id) {
        boardRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionConstant.BOARD_NOT_FOUND))
            .increaseHit();
    }

    @Transactional
//    @Caching(evict = {
//        @CacheEvict(value = "boards", allEntries = true),
//        @CacheEvict(value = "boardsForAdmin", allEntries = true)})
    public void register(BoardRequest boardRequest, List<MultipartFile> files) {
        Board board = boardRequestMapper.toEntity(boardRequest);

        Member member = memberRepository.findByEmailAndStatus(boardRequest.getEmail(), Status.ACTIVE)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionConstant.MEMBER_NOT_FOUND));
        board.setMember(member);

        List<Image> images = imageProcessor.parse(files);
        board.addImages(images);

        boardRepository.save(board);
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "boards"),
        @CacheEvict(value = "boardsForAdmin")})
    public BoardResponse edit(Long id, BoardModifyRequest modifyRequest) {
        Board board = boardRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionConstant.BOARD_NOT_FOUND));

        boardModifyMapper.updateFromDto(modifyRequest, board);
        return boardResponseMapper.toDto(board);
    }

    @Transactional
//    @Caching(evict = {
//        @CacheEvict(value = "boards", allEntries = true),
//        @CacheEvict(value = "boardsForAdmin", allEntries = true)})
    public void deleteSoftly(Long id) {
        boardRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionConstant.BOARD_NOT_FOUND))
            .delete();
    }

    @Transactional
//    @Caching(evict = {
//        @CacheEvict(value = "boards", allEntries = true),
//        @CacheEvict(value = "boardsForAdmin", allEntries = true)})
    public void restore(Long id) {
        boardRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionConstant.BOARD_NOT_FOUND))
            .restore();
    }


    /* FOR ADMIN */
    @Transactional(readOnly = true)
//    @Cacheable(keyGenerator = "customKeyGenerator", value = "boardsForAdmin", unless = "#result==null", cacheManager = "cacheManager")
    public List<BoardResponse> findListBySearchConditionForAdmin(BoardSearchCondition condition) {
        List<Board> boards = boardRepository.findByConditionForAdmin(condition);
        return boardResponseMapper.toDtoList(boards);
    }
}