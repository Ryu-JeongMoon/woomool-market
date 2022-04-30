package com.woomoolmarket.domain.repository;

import com.woomoolmarket.domain.entity.Board;
import com.woomoolmarket.domain.entity.enumeration.Status;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

  @Query("select b from Board b join fetch b.member where b.id = :id and b.status = :status")
  Optional<Board> findByIdAndStatus(Long id, Status status);
}
