package com.woomoolmarket.entity.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.woomoolmarket.entity.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.woomoolmarket.entity.member.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Member> findByNickname(String nickname) {
        return queryFactory.select(member)
                           .where(member.nickname.eq(nickname))
                           .fetch();
    }

    // TODO batch 로 변환?
    @Override
    public void deleteMemberHardly() {
        queryFactory.delete(member)
                    .where(member.leaveDate.loe(LocalDateTime.now()
                                                             .minusMonths(12)))
                    .execute();
    }

    @Override
    public Optional<Long> findPreviousId(Long id) {
        return Optional.of(queryFactory.select(member.id.max())
                                       .from(member)
                                       .where(member.id.lt(id))
                                       .fetchOne());
    }

    @Override
    public Optional<Long> findNextId(Long id) {
        return Optional.of(queryFactory.select(member.id.min())
                                       .from(member)
                                       .where(member.id.gt(id))
                                       .fetchOne());
    }
}
