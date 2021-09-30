package com.woomoolmarket.domain.member.repository;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByPhone(String phone);

    Optional<Member> findByNickname(String nickname);

    @Query(value = "select m from Member m where m.status = :status",
        countQuery = "select count(m.id) from Member m where m.status = :status")
    Page<Member> findMembersByStatus(@Param("status") Status status, Pageable pageable);

    @Query("select m from Member m where m.status = :status")
    List<Member> findMembersByStatusAndCache(@Param("status") Status status);
}
