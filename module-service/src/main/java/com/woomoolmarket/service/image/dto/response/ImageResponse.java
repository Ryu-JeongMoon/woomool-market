package com.woomoolmarket.service.image.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ImageResponse {

  private Long id;

  private Long fileSize;

  private String filePath;

  private String fileName;

  private String originalFileName;
}
