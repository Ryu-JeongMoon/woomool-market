package com.woomoolmarket.service.image;

import com.woomoolmarket.domain.entity.enumeration.Status;
import com.woomoolmarket.domain.entity.Image;
import com.woomoolmarket.domain.repository.ImageRepository;
import com.woomoolmarket.service.image.dto.response.ImageResponse;
import com.woomoolmarket.service.image.mapper.ImageResponseMapper;
import com.woomoolmarket.util.constants.ExceptionMessages;
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
      .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.Image.NOT_FOUND))
      .delete();
  }

  @Transactional
  public void restoreByImageId(Long imageId) {
    imageRepository.findByIdAndStatus(imageId, Status.ACTIVE)
      .orElseThrow(() -> new EntityNotFoundException(ExceptionMessages.Image.NOT_FOUND))
      .restore();
  }
}
