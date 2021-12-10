package com.woomoolmarket.domain.purchase.order.repository;

import static com.woomoolmarket.domain.member.entity.QMember.member;
import static com.woomoolmarket.domain.purchase.order.entity.QOrder.order;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woomoolmarket.common.util.QueryDslUtils;
import com.woomoolmarket.domain.purchase.order.entity.Order;
import com.woomoolmarket.domain.purchase.order.entity.OrderStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<Order> findByConditionForAdmin(OrderSearchCondition searchCondition) {
        return queryFactory.selectFrom(order)
            .leftJoin(order.member, member)
            .fetchJoin()
            .where(searchByAll(searchCondition))
            .fetch();
    }

    private BooleanBuilder memberIdEq(Long memberId) {
        return QueryDslUtils.nullSafeBuilder(() -> order.member.id.eq(memberId));
    }

    private BooleanBuilder emailContains(String email) {
        return QueryDslUtils.nullSafeBuilder(() -> order.member.email.contains(email));
    }

    private BooleanBuilder statusEq(OrderStatus orderStatus) {
        return QueryDslUtils.nullSafeBuilder(() -> order.orderStatus.eq(orderStatus));
    }

    private BooleanBuilder searchByAll(OrderSearchCondition searchCondition) {
        return memberIdEq(searchCondition.getMemberId())
            .and(emailContains(searchCondition.getEmail()))
            .and(statusEq(searchCondition.getOrderStatus()));
    }
}
