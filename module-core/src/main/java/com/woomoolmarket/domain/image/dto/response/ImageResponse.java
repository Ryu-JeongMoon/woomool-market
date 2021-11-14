package com.woomoolmarket.domain.image.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageResponse {

    private Long id;

    private Long fileSize;

    private String filePath;

    private String fileName;

    private String originalFileName;
}
