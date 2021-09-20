package com.woomoolmarket.domain.member.repository;

import com.woomoolmarket.domain.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("select m from Member m where m.leaveDateTime is not null")
    List<Member> findInactiveMembers();

    @Query("select m from Member m where m.leaveDateTime is null")
    List<Member> findActiveMembers();
}
