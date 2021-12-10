package com.woomoolmarket.domain.image.repository;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.image.entity.Image;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("select i from Image i join fetch i.board where i.board.id = :boardId and i.status = :status")
    List<Image> findByBoardIdAndStatus(Long boardId, Status status);

    Optional<Image> findByIdAndStatus(Long id, Status status);
}
