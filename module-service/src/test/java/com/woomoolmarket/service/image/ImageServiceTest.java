package com.woomoolmarket.service.image;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.config.ServiceTestConfig;
import com.woomoolmarket.domain.board.entity.Board;
import com.woomoolmarket.domain.image.entity.Image;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.service.image.dto.response.ImageResponse;
import java.util.List;
import java.util.Objects;
import javax.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Log4j2
class ImageServiceTest extends ServiceTestConfig {

  @BeforeEach
  void init() {
    Member member = memberTestHelper.createMember();
    Board board = boardTestHelper.createBoard(member);
    BOARD_ID = board.getId();

    Image image = Image.builder()
      .fileName("fileName")
      .originalFileName("originalFileName")
      .filePath("filePath")
      .fileSize(5000L)
      .build();
    board.addImages(List.of(image));
  }

  @AfterEach
  void clear() {
    Objects.requireNonNull(stringRedisTemplate.keys("*")).forEach(k -> stringRedisTemplate.delete(k));
  }

  @Test
  @DisplayName("게시글 번호에 의한 이미지 조회 성공")
  void findByBoard() {
    List<ImageResponse> imageResponses = imageService.findByBoard(BOARD_ID);
    assertThat(imageResponses.size()).isEqualTo(1);
  }

  @Test
  @DisplayName("존재하지 않는 게시글 번호에 의한 이미지 조회 - size 0")
  void findByBoard_Fail() {
    List<ImageResponse> imageResponses = imageService.findByBoard(0L);
    assertThat(imageResponses.size()).isEqualTo(0);
  }

  @Test
  @DisplayName("이미지 번호에 의한 삭제 성공")
  void deleteByImageId() {
    Image image = imageRepository.findByBoardIdAndStatus(BOARD_ID, Status.ACTIVE).get(0);
    imageService.deleteByImageId(image.getId());

    List<ImageResponse> imageResponses = imageService.findByBoard(BOARD_ID);
    assertThat(imageResponses.size()).isEqualTo(0);
  }

  @Test
  @DisplayName("존재하지 않는 이미지 번호에 의한 삭제 - EntityNotFoundException 예외 발생")
  void deleteByImageId_Fail() {
    assertThrows(EntityNotFoundException.class, () -> imageService.deleteByImageId(0L));
  }
}