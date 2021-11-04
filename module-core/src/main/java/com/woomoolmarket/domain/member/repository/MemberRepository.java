package com.woomoolmarket.domain.member.repository;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByEmailAndStatus(String email, Status status);

    Optional<Member> findByPhoneAndStatus(String phone, Status status);

    Optional<Member> findByIdAndStatus(Long id, Status status);

}
