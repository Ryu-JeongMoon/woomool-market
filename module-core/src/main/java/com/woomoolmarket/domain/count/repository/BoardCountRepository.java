package com.woomoolmarket.domain.count.repository;

import com.woomoolmarket.domain.count.entity.BoardCount;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCountRepository extends JpaRepository<BoardCount, Long> {

    Optional<BoardCount> findByBoardId(Long boardId);
}
