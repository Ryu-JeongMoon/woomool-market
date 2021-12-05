package com.woomoolmarket.domain.member.repository;

import static com.woomoolmarket.domain.member.entity.QMember.member;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.common.util.QueryDslUtil;
import com.woomoolmarket.domain.member.entity.AuthProvider;
import com.woomoolmarket.domain.member.entity.Authority;
import com.woomoolmarket.domain.member.entity.Member;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Long> findPreviousId(Long id) {
        return Optional.ofNullable(queryFactory.select(member.id.max())
            .from(member)
            .where(member.id.lt(id))
            .fetchOne());
    }

    @Override
    public Optional<Long> findNextId(Long id) {
        return Optional.ofNullable(queryFactory.select(member.id.min())
            .from(member)
            .where(member.id.gt(id))
            .fetchOne());
    }

    // 이놈으로 Batch Job
    @Override
    public void deleteMemberHardly() {
        queryFactory.delete(member)
            .where(member.leaveDateTime.lt(LocalDateTime.now().minusMonths(6)))
            .execute();
    }

    @Override
    public List<Member> findByConditionForAdmin(MemberSearchCondition searchCondition) {
        return queryFactory.selectFrom(member)
            .where(searchByAllForAdmin(searchCondition))
            .fetch();
    }

    private BooleanBuilder emailContains(String email) {
        return QueryDslUtil.nullSafeBuilder(() -> member.email.contains(email));
    }

    private BooleanBuilder nicknameContains(String nickname) {
        return QueryDslUtil.nullSafeBuilder(() -> member.nickname.contains(nickname));
    }

    private BooleanBuilder phoneContains(String phone) {
        return QueryDslUtil.nullSafeBuilder(() -> member.phone.contains(phone));
    }

    private BooleanBuilder licenseContains(String license) {
        return QueryDslUtil.nullSafeBuilder(() -> member.license.contains(license));
    }

    private BooleanBuilder statusEq(Status status) {
        return QueryDslUtil.nullSafeBuilder(() -> member.status.eq(status));
    }

    private BooleanBuilder authProviderEq(AuthProvider provider) {
        return QueryDslUtil.nullSafeBuilder(() -> member.provider.eq(provider));
    }

    private BooleanBuilder authorityEq(Authority authority) {
        return QueryDslUtil.nullSafeBuilder(() -> member.authority.eq(authority));
    }

    private BooleanBuilder searchByAllForAdmin(MemberSearchCondition searchCondition) {
        return emailContains(searchCondition.getEmail())
            .and(nicknameContains(searchCondition.getNickname()))
            .and(phoneContains(searchCondition.getPhone()))
            .and(licenseContains(searchCondition.getLicense()))
            .and(statusEq(searchCondition.getStatus()))
            .and(authorityEq(searchCondition.getAuthority()))
            .and(authProviderEq(searchCondition.getProvider()));
    }
}
