package com.woomoolmarket.service.board;

import com.woomoolmarket.cache.CacheService;
import com.woomoolmarket.common.constants.CacheConstants;
import com.woomoolmarket.common.constants.ExceptionConstants;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.board.repository.BoardRepository;
import com.woomoolmarket.domain.count.repository.BoardCountRepository;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardCountService {

  private final CacheService cacheService;
  private final BoardRepository boardRepository;
  private final BoardCountRepository boardCountRepository;

  @Transactional(readOnly = true)
  @Cacheable(key = "customKeyGenerator", cacheManager = "gsonCacheManager")
  public int getHit(Long boardId) {
    return boardCountRepository.findByBoardId(boardId)
      .orElseThrow(() -> new EntityNotFoundException(ExceptionConstants.BOARD_NOT_FOUND))
      .getHitCount();
  }

  @Transactional
  public void increaseHit(Long boardId) {
    cacheService.increment(CacheConstants.BOARD_HIT_COUNT + boardId);
  }

  @Transactional
  public void synchronizeHit(Long boardId) {
    int hit = cacheService.getDataAsInt(CacheConstants.BOARD_HIT_COUNT + boardId);

    boardRepository.findByIdAndStatus(boardId, Status.ACTIVE)
      .orElseThrow(() -> new EntityNotFoundException(ExceptionConstants.BOARD_NOT_FOUND))
      .changeHit(hit);
  }
}
