package com.woomoolmarket.service.image;

import com.woomoolmarket.util.constants.ExceptionConstants;
import com.woomoolmarket.domain.enumeration.Status;
import com.woomoolmarket.domain.image.entity.Image;
import com.woomoolmarket.domain.image.repository.ImageRepository;
import com.woomoolmarket.service.image.dto.response.ImageResponse;
import com.woomoolmarket.service.image.mapper.ImageResponseMapper;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImageService {

  private final ImageRepository imageRepository;
  private final ImageResponseMapper imageResponseMapper;

  @Transactional(readOnly = true)
  public List<ImageResponse> findByBoard(Long boardId) {
    List<Image> images = imageRepository.findByBoardIdAndStatus(boardId, Status.ACTIVE);
    return imageResponseMapper.toDtoList(images);
  }

  @Transactional
  public void deleteByImageId(Long imageId) {
    imageRepository.findByIdAndStatus(imageId, Status.ACTIVE)
      .orElseThrow(() -> new EntityNotFoundException(ExceptionConstants.IMAGE_NOT_FOUND))
      .delete();
  }

  @Transactional
  public void restoreByImageId(Long imageId) {
    imageRepository.findByIdAndStatus(imageId, Status.ACTIVE)
      .orElseThrow(() -> new EntityNotFoundException(ExceptionConstants.IMAGE_NOT_FOUND))
      .restore();
  }
}
