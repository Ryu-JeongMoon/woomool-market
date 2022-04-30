package com.woomoolmarket.service.board;

import com.woomoolmarket.domain.entity.enumeration.Status;
import com.woomoolmarket.domain.port.CacheTokenPort;
import com.woomoolmarket.domain.repository.BoardCountRepository;
import com.woomoolmarket.domain.repository.BoardRepository;
import com.woomoolmarket.util.constants.CacheConstants;
import com.woomoolmarket.util.constants.ExceptionMessages;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardCountService {

  private final CacheTokenPort cacheTokenPort;
  private final BoardRepository boardRepository;
  private final BoardCountRepository boardCountRepository;

  @Transactional(readOnly = true)
  @Cacheable(key = "customKeyGenerator", cacheManager = "gsonCacheManager")
  public int getHit(Long boardId) {
    return boardCountRepository.findByBoardId(boardId)
      .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.Board.NOT_FOUND))
      .getHitCount();
  }

  @Transactional
  public void increaseHit(Long boardId) {
    cacheTokenPort.increment(CacheConstants.BOARD_HIT_COUNT + boardId);
  }

  @Transactional
  public void synchronizeHit(Long boardId) {
    int hit = cacheTokenPort.getDataAsInt(CacheConstants.BOARD_HIT_COUNT + boardId);

    boardRepository.findByIdAndStatus(boardId, Status.ACTIVE)
      .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.Board.NOT_FOUND))
      .changeHit(hit);
  }
}
