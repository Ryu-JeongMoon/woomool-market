package com.woomoolmarket.model.member.repository;

import com.woomoolmarket.model.member.entity.Member;
import java.util.List;
import java.util.Optional;

public interface MemberRepositoryCustom {

    List<Member> findByNickname(String nickname);

    void deleteMemberHardly();

    Optional<Long> findPreviousId(Long id);

    Optional<Long> findNextId(Long id);
}
