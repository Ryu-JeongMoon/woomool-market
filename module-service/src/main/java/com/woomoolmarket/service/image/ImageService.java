package com.woomoolmarket.service.image;

import com.woomoolmarket.common.constant.ExceptionConstant;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.board.repository.BoardRepository;
import com.woomoolmarket.domain.image.repository.ImageRepository;
import com.woomoolmarket.service.image.dto.response.ImageResponse;
import com.woomoolmarket.service.image.mapper.ImageResponseMapper;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final BoardRepository boardRepository;
    private final ImageRepository imageRepository;
    private final ImageResponseMapper imageResponseMapper;

    @Transactional(readOnly = true)
    public List<ImageResponse> findByBoard(Long boardId) {
        return imageRepository.findByBoardIdAndStatus(boardId, Status.ACTIVE)
            .stream()
            .map(imageResponseMapper::toDto)
            .collect(Collectors.toList());
    }

    public void deleteByImageId(Long imageId) {
        imageRepository.findByIdAndStatus(imageId, Status.ACTIVE)
            .orElseThrow(() -> new EntityNotFoundException(ExceptionConstant.IMAGE_NOT_FOUND))
            .changeStatus(Status.INACTIVE);
    }
}
