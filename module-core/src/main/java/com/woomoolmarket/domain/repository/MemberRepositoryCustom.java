package com.woomoolmarket.domain.repository;

import com.woomoolmarket.domain.repository.querydto.MemberQueryResponse;
import com.woomoolmarket.domain.repository.querydto.MemberSearchCondition;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {

  Optional<Long> findNextId(Long id);

  Optional<Long> findPreviousId(Long id);

  void deleteMemberHardly();

  Page<MemberQueryResponse> searchForAdminBy(MemberSearchCondition condition, Pageable pageable);
}
