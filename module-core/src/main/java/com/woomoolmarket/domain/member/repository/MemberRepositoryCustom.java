package com.woomoolmarket.domain.member.repository;

import java.util.Optional;

public interface MemberRepositoryCustom {

    Optional<Long> findNextId(Long id);

    Optional<Long> findPreviousId(Long id);

    void deleteMemberHardly();
}
