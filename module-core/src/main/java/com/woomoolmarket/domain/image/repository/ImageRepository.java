package com.woomoolmarket.domain.image.repository;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.image.entity.Image;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByBoardIdAndStatus(Long boardId, Status status);

    Optional<Image> findByIdAndStatus(Long id, Status status);
}
