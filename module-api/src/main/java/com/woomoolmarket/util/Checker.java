package com.woomoolmarket.util;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.common.util.ExceptionUtil;
import com.woomoolmarket.domain.board.entity.Board;
import com.woomoolmarket.domain.board.entity.BoardCategory;
import com.woomoolmarket.domain.board.repository.BoardRepository;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import com.woomoolmarket.domain.purchase.product.entity.Product;
import com.woomoolmarket.domain.purchase.product.repository.ProductRepository;
import com.woomoolmarket.service.board.dto.request.BoardRequest;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Checker {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final ProductRepository productRepository;

    public boolean isSelf(Long memberId) {
        Member member = memberRepository.findByIdAndStatus(memberId, Status.ACTIVE)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.MEMBER_NOT_FOUND));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        return member.getEmail().equals(principal.getUsername());
    }

    public boolean isSelfByBoardId(Long boardId) {
        Board board = boardRepository.findByIdAndStatus(boardId, Status.ACTIVE)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.BOARD_NOT_FOUND));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();

        return board.getMember().getEmail().equals(principal.getUsername());
    }

    public boolean isSelfByProductId(Long productId) {
        Product product = productRepository.findByIdAndStatus(productId, Status.ACTIVE)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionUtil.PRODUCT_NOT_FOUND));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();

        return product.getMember().getEmail().equals(principal.getUsername());
    }

    public boolean isQnaOrFree(BoardRequest boardRequest) {
        return boardRequest.getBoardCategory() == BoardCategory.FREE || boardRequest.getBoardCategory() == BoardCategory.QNA;
    }
}
