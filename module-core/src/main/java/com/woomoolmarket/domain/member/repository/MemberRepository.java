package com.woomoolmarket.domain.member.repository;

import com.woomoolmarket.domain.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("select m from Member m where m.leaveDateTime is not null order by m.id")
    Page<Member> findInactiveMembers(Pageable pageable);

    @Query("select m from Member m where m.leaveDateTime is null order by m.id")
    Page<Member> findActiveMembers(Pageable pageable);
}
