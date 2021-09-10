package com.woomoolmarket.model.member.repository;

import com.woomoolmarket.model.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("select m from Member m join fetch m.authorities where m.email = :email")
    Optional<Member> findWithAuthoritiesByUsername(@Param("email") String email);
}
