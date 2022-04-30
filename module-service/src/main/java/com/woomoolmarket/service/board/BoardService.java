package com.woomoolmarket.service.board;

import static com.woomoolmarket.util.constants.CacheConstants.BOARDS;
import static com.woomoolmarket.util.constants.CacheConstants.BOARDS_FOR_ADMIN;

import com.woomoolmarket.domain.port.CacheTokenPort;
import com.woomoolmarket.domain.entity.Board;
import com.woomoolmarket.domain.repository.querydto.BoardQueryResponse;
import com.woomoolmarket.domain.repository.BoardRepository;
import com.woomoolmarket.domain.repository.querydto.BoardSearchCondition;
import com.woomoolmarket.domain.entity.enumeration.Status;
import com.woomoolmarket.domain.entity.Image;
import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.repository.MemberRepository;
import com.woomoolmarket.service.board.dto.request.BoardModifyRequest;
import com.woomoolmarket.service.board.dto.request.BoardRequest;
import com.woomoolmarket.service.board.dto.response.BoardResponse;
import com.woomoolmarket.service.board.mapper.BoardModifyMapper;
import com.woomoolmarket.service.board.mapper.BoardRequestMapper;
import com.woomoolmarket.service.board.mapper.BoardResponseMapper;
import com.woomoolmarket.service.image.ImageProcessor;
import com.woomoolmarket.util.constants.ExceptionMessages;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BoardService {

  private final CacheTokenPort cacheTokenPort;
  private final ImageProcessor imageProcessor;
  private final MemberRepository memberRepository;

  private final BoardRepository boardRepository;
  private final BoardModifyMapper boardModifyMapper;
  private final BoardRequestMapper boardRequestMapper;
  private final BoardResponseMapper boardResponseMapper;

  @Transactional(readOnly = true)
  @Cacheable(keyGenerator = "customKeyGenerator", cacheNames = BOARDS, unless = "#result.content == null")
  public Page<BoardQueryResponse> searchBy(BoardSearchCondition searchCondition, Pageable pageable) {
    return boardRepository.searchBy(searchCondition, pageable);
  }

  @Transactional(readOnly = true)
  public BoardResponse findBy(Long id, Status status) {
    return boardRepository.findByIdAndStatus(id, status)
      .map(boardResponseMapper::toDto)
      .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.Board.NOT_FOUND));
  }

  @Transactional
  @CacheEvict(cacheNames = { BOARDS, BOARDS_FOR_ADMIN }, allEntries = true)
  public void write(BoardRequest boardRequest, List<MultipartFile> files) {
    Board board = boardRequestMapper.toEntity(boardRequest);

    Member member = memberRepository.findByEmailAndStatus(boardRequest.getEmail(), Status.ACTIVE)
      .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.Member.NOT_FOUND));
    board.registerMember(member);

    List<Image> images = imageProcessor.parse(files);
    board.addImages(images);

    boardRepository.save(board);
  }

  @Transactional
  public void increaseHitByDB(Long id) {
    boardRepository.findById(id)
      .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.Board.NOT_FOUND))
      .increaseHit();
  }

  @Transactional
  public void increaseHitByRedis(Long id) {
    // TODO, redis 에서 조회수 증가
    cacheTokenPort.increment("panda");
  }

  @Transactional
  @CacheEvict(cacheNames = { BOARDS, BOARDS_FOR_ADMIN }, allEntries = true)
  public BoardResponse edit(Long id, BoardModifyRequest modifyRequest) {
    Board board = boardRepository.findById(id)
      .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.Board.NOT_FOUND));

    boardModifyMapper.updateFromDto(modifyRequest, board);
    return boardResponseMapper.toDto(board);
  }

  @Transactional
  @CacheEvict(cacheNames = { BOARDS, BOARDS_FOR_ADMIN }, allEntries = true)
  public void delete(Long id) {
    boardRepository.findById(id)
      .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.Board.NOT_FOUND))
      .delete();
  }

  @Transactional
  @CacheEvict(cacheNames = { BOARDS, BOARDS_FOR_ADMIN }, allEntries = true)
  public void restore(Long id) {
    boardRepository.findById(id)
      .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.Board.NOT_FOUND))
      .restore();
  }


  /* FOR ADMIN */
  @Transactional(readOnly = true)
  @Cacheable(keyGenerator = "customKeyGenerator", cacheNames = BOARDS_FOR_ADMIN, unless = "#result.content == null")
  public Page<BoardQueryResponse> searchForAdminBy(BoardSearchCondition condition, Pageable pageable) {
    return boardRepository.searchForAdminBy(condition, pageable);
  }
}