package com.woomoolmarket.domain.board.dto.response;

import com.woomoolmarket.domain.image.entity.Image;
import java.time.LocalDateTime;
import java.util.List;

public class BoardImageResponse {

    private Long id;

    private String title;

    private String content;

    private LocalDateTime createdDateTime;

    private int hit;

    private String username;

    private List<Image> images;
}

