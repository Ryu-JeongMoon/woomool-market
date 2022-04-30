package com.woomoolmarket.domain.repository;

import com.woomoolmarket.domain.entity.Image;
import com.woomoolmarket.domain.entity.enumeration.Status;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImageRepository extends JpaRepository<Image, Long> {

  @Query("select i from Image i join fetch i.board where i.board.id = :boardId and i.status = :status")
  List<Image> findByBoardIdAndStatus(Long boardId, Status status);

  Optional<Image> findByIdAndStatus(Long id, Status status);
}
