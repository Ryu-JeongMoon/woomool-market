package com.woomoolmarket.controller.image;

import com.woomoolmarket.service.image.ImageService;
import com.woomoolmarket.service.image.dto.response.ImageResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/images")
public class ImageController {

  private final ImageService imageService;

  @GetMapping("/{boardId}")
  public ResponseEntity<CollectionModel<ImageResponse>> getImages(@PathVariable Long boardId) {
    List<ImageResponse> imageResponses = imageService.findByBoard(boardId);
    CollectionModel<ImageResponse> collectionModel = CollectionModel.of(imageResponses);
    return ResponseEntity.ok(collectionModel);
  }

  @DeleteMapping("/{imageId}")
  public ResponseEntity<Void> deleteByImage(@PathVariable Long imageId) {
    imageService.deleteByImageId(imageId);
    return ResponseEntity.noContent().build();
  }
}
