package com.woomoolmarket.domain.purchase.cart.repository;

import static com.woomoolmarket.domain.purchase.cart.entity.QCart.cart;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woomoolmarket.common.util.CustomPageImpl;
import com.woomoolmarket.common.util.QueryDslUtils;
import com.woomoolmarket.domain.purchase.cart.query.CartQueryResponse;
import com.woomoolmarket.domain.purchase.cart.query.QCartQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  private Page<CartQueryResponse> searchTemplateBy(BooleanBuilder booleanBuilder, Pageable pageable) {
    QueryResults<CartQueryResponse> results = queryFactory
      .select(new QCartQueryResponse(cart.id, cart.member, cart.product, cart.quantity))
      .from(cart)
      .leftJoin(cart.member)
      .leftJoin(cart.product)
      .where(booleanBuilder)
      .offset(pageable.getOffset())
      .limit(pageable.getPageSize())
      .orderBy(cart.id.desc())
      .fetchResults();

    return new CustomPageImpl<>(results.getResults(), pageable.getPageNumber(), pageable.getPageSize(), results.getTotal());
  }

  @Override
  public Page<CartQueryResponse> searchBy(Long memberId, Pageable pageable) {
    return searchTemplateBy(memberIdEquals(memberId), pageable);
  }

  @Override
  public Page<CartQueryResponse> searchForAdminBy(Pageable pageable) {
    return searchTemplateBy(null, pageable);
  }

  private BooleanBuilder memberIdEquals(Long memberId) {
    return QueryDslUtils.nullSafeBuilder(() -> cart.member.id.eq(memberId));
  }
}
