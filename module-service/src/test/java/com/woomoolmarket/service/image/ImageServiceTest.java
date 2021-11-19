package com.woomoolmarket.service.image;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.board.entity.Board;
import com.woomoolmarket.domain.board.entity.BoardCategory;
import com.woomoolmarket.domain.board.repository.BoardRepository;
import com.woomoolmarket.domain.image.dto.response.ImageResponse;
import com.woomoolmarket.domain.image.entity.Image;
import com.woomoolmarket.domain.image.repository.ImageRepository;
import com.woomoolmarket.domain.member.entity.Member;
import com.woomoolmarket.domain.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Log4j2
@Transactional
@SpringBootTest
class ImageServiceTest {

    private static Long BOARD_ID;
    private final String MEMBER_EMAIL = "panda@naver.com";
    private final String BOARD_TITLE = "panda";
    private final String BOARD_CONTENT = "bear";

    @Autowired
    ImageService imageService;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @BeforeEach
    void init() {
        Member member = Member.builder()
            .email(MEMBER_EMAIL)
            .password("123456")
            .build();
        memberRepository.save(member);

        Image image = Image.builder()
            .fileName("fileName")
            .originalFileName("originalFileName")
            .filePath("filePath")
            .fileSize(5000L)
            .build();

        Board board = Board.builder()
            .member(member)
            .title(BOARD_TITLE)
            .content(BOARD_CONTENT)
            .startDateTime(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
            .endDateTime(LocalDateTime.of(2099, 1, 1, 1, 1, 1))
            .boardCategory(BoardCategory.FREE)
            .build();

        board.addImages(List.of(image));
        BOARD_ID = boardRepository.save(board).getId();
    }

    @AfterEach
    void clear() {
        imageRepository.deleteAll();
        boardRepository.deleteAll();
        memberRepository.deleteAll();

        em.createNativeQuery("ALTER TABLE IMAGE ALTER COLUMN `image_id` RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE BOARD ALTER COLUMN `board_id` RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE MEMBER ALTER COLUMN `member_id` RESTART WITH 1").executeUpdate();
    }

    // TODO, 보완 필요, org.springframework.dao.DataIntegrityViolationException 발생
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
        Image image = imageRepository.findByBoardIdAndStatus(BOARD_ID, Status.ACTIVE).get();
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