package com.woomoolmarket.domain.member.repository;

import com.woomoolmarket.domain.member.entity.Member;
import java.util.List;
import java.util.Optional;

public interface MemberRepositoryCustom {

    Optional<Long> findNextId(Long id);

    Optional<Long> findPreviousId(Long id);

    void deleteMemberHardly();

    List<Member> findByConditionForAdmin(MemberSearchCondition searchCondition);
}
