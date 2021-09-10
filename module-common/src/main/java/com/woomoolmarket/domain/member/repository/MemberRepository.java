package com.woomoolmarket.domain.member.repository;

import com.woomoolmarket.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);
}
