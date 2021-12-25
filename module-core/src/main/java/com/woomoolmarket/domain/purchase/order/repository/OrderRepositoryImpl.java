package com.woomoolmarket.domain.purchase.order.repository;

import static com.woomoolmarket.domain.purchase.order.entity.QOrder.order;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woomoolmarket.common.util.CustomPageImpl;
import com.woomoolmarket.common.util.QueryDslUtils;
import com.woomoolmarket.domain.purchase.order.entity.OrderStatus;
import com.woomoolmarket.domain.purchase.order.query.OrderQueryResponse;
import com.woomoolmarket.domain.purchase.order.query.QOrderQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public Page<OrderQueryResponse> searchTemplateBy(BooleanBuilder booleanBuilder, Pageable pageable) {
        QueryResults<OrderQueryResponse> results =
            queryFactory.select(
                    new QOrderQueryResponse(order))
                .from(order)
                .join(order.member)
                .where(booleanBuilder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(order.id.desc())
                .fetchResults();

        return new CustomPageImpl<>(results.getResults(), pageable.getPageNumber(), pageable.getPageSize(), results.getTotal());
    }

    @Override
    public Page<OrderQueryResponse> searchBy(Long memberId, Pageable pageable) {
        return searchTemplateBy(combineForSelfBy(memberId), pageable);
    }

    @Override
    public Page<OrderQueryResponse> searchForAdminBy(OrderSearchCondition condition, Pageable pageable) {
        return searchTemplateBy(combineForAdminBy(condition), pageable);
    }

    private BooleanBuilder memberIdEquals(Long memberId) {
        return QueryDslUtils.nullSafeBuilder(() -> order.member.id.eq(memberId));
    }

    private BooleanBuilder emailContains(String email) {
        return QueryDslUtils.nullSafeBuilder(() -> order.member.email.contains(email));
    }

    private BooleanBuilder statusEquals(OrderStatus orderStatus) {
        return QueryDslUtils.nullSafeBuilder(() -> order.orderStatus.eq(orderStatus));
    }

    private BooleanBuilder combineForSelfBy(Long memberId) {
        return memberIdEquals(memberId);
    }

    private BooleanBuilder combineForAdminBy(OrderSearchCondition condition) {
        return memberIdEquals(condition.getMemberId())
            .and(emailContains(condition.getEmail()))
            .and(statusEquals(condition.getOrderStatus()));
    }
}
