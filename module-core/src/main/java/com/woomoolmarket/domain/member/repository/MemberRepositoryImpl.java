package com.woomoolmarket.domain.member.repository;

import static com.woomoolmarket.domain.member.entity.QMember.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // 이놈으로 Batch Job
    @Override
    public void deleteMemberHardly() {
        queryFactory.delete(member)
            .where(member.leaveDateTime.lt(LocalDateTime.now().minusMonths(6)))
            .execute();
    }

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
}
