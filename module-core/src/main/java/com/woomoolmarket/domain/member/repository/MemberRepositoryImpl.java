package com.woomoolmarket.domain.member.repository;

import static com.woomoolmarket.domain.member.entity.QMember.member;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.common.util.CustomPageImpl;
import com.woomoolmarket.common.util.QueryDslUtils;
import com.woomoolmarket.domain.member.entity.AuthProvider;
import com.woomoolmarket.domain.member.entity.Authority;
import com.woomoolmarket.domain.member.query.MemberQueryResponse;
import com.woomoolmarket.domain.member.query.QMemberQueryResponse;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<MemberQueryResponse> searchForAdminBy(MemberSearchCondition condition, Pageable pageable) {
        QueryResults<MemberQueryResponse> results =
            queryFactory.select(
                    new QMemberQueryResponse(member.id, member.email, member.nickname, member.profileImage, member.phone,
                        member.license, member.address, member.authProvider, member.status, member.authority,
                        member.createdDateTime, member.lastModifiedDateTime, member.leaveDateTime))
                .from(member)
                .where(combineForAdminBy(condition))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(member.id.desc())
                .fetchResults();

        return new CustomPageImpl<>(results.getResults(), pageable.getPageNumber(), pageable.getPageSize(), results.getTotal());
    }

    private BooleanBuilder emailContains(String email) {
        return QueryDslUtils.nullSafeBuilder(() -> member.email.contains(email));
    }

    private BooleanBuilder nicknameContains(String nickname) {
        return QueryDslUtils.nullSafeBuilder(() -> member.nickname.contains(nickname));
    }

    private BooleanBuilder phoneContains(String phone) {
        return QueryDslUtils.nullSafeBuilder(() -> member.phone.contains(phone));
    }

    private BooleanBuilder licenseContains(String license) {
        return QueryDslUtils.nullSafeBuilder(() -> member.license.contains(license));
    }

    private BooleanBuilder statusEq(Status status) {
        return QueryDslUtils.nullSafeBuilder(() -> member.status.eq(status));
    }

    private BooleanBuilder authProviderEq(AuthProvider provider) {
        return QueryDslUtils.nullSafeBuilder(() -> member.authProvider.eq(provider));
    }

    private BooleanBuilder authorityEq(Authority authority) {
        return QueryDslUtils.nullSafeBuilder(() -> member.authority.eq(authority));
    }

    private BooleanBuilder combineForAdminBy(MemberSearchCondition searchCondition) {
        return emailContains(searchCondition.getEmail())
            .and(nicknameContains(searchCondition.getNickname()))
            .and(phoneContains(searchCondition.getPhone()))
            .and(licenseContains(searchCondition.getLicense()))
            .and(statusEq(searchCondition.getStatus()))
            .and(authorityEq(searchCondition.getAuthority()))
            .and(authProviderEq(searchCondition.getProvider()));
    }
}
