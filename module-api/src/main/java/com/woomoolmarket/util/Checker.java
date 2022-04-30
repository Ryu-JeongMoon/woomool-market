package com.woomoolmarket.util;

import com.woomoolmarket.domain.entity.Board;
import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.entity.Product;
import com.woomoolmarket.domain.entity.enumeration.BoardCategory;
import com.woomoolmarket.domain.entity.enumeration.Status;
import com.woomoolmarket.domain.repository.BoardRepository;
import com.woomoolmarket.domain.repository.MemberRepository;
import com.woomoolmarket.domain.repository.ProductRepository;
import com.woomoolmarket.service.board.dto.request.BoardRequest;
import com.woomoolmarket.util.constants.ExceptionMessages;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Checker {

  private final BoardRepository boardRepository;
  private final MemberRepository memberRepository;
  private final ProductRepository productRepository;

  public boolean isSelf(Long memberId) {
    Member member = memberRepository.findByIdAndStatus(memberId, Status.ACTIVE)
      .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.Member.NOT_FOUND));
    return check(member.getEmail());
  }

  public boolean isSelfByEmail(String email) {
    Member member = memberRepository.findByEmailAndStatus(email, Status.ACTIVE)
      .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.Member.NOT_FOUND));
    return check(member.getEmail());
  }

  public boolean isSelfByBoardId(Long boardId) {
    Board board = boardRepository.findByIdAndStatus(boardId, Status.ACTIVE)
      .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.Board.NOT_FOUND));
    return check(board.getMember().getEmail());
  }

  public boolean isSelfByProductId(Long productId) {
    Product product = productRepository.findByIdAndStatus(productId, Status.ACTIVE)
      .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.Product.NOT_FOUND));
    return check(product.getMember().getEmail());
  }

  private boolean check(String email) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return email.equals(username);
  }

  public boolean isQnaOrFree(BoardRequest boardRequest) {
    return boardRequest.getBoardCategory() == BoardCategory.FREE || boardRequest.getBoardCategory() == BoardCategory.QNA;
  }
}
