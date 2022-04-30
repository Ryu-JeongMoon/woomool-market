package com.woomoolmarket.domain.repository;

import com.woomoolmarket.domain.entity.Member;
import com.woomoolmarket.domain.entity.enumeration.Status;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

  @Query("select m from Member m where m.email = :email and m.status = 'ACTIVE'")
  Optional<Member> findActiveByEmail(String email);

  boolean existsByEmail(String email);

  @Query("select m.id from Member m where m.email = :email")
  Optional<Long> findIdByEmail(String email);

  Optional<Member> findByEmail(String email);

  Optional<Member> findByEmailAndStatus(String email, Status status);

  Optional<Member> findByPhoneAndStatus(String phone, Status status);

  Optional<Member> findByIdAndStatus(Long id, Status status);

}
