package com.woomoolmarket.helper;

import com.woomoolmarket.domain.image.entity.Image;

public class ImageTestHelper {

  public static final String FILE_NAME = "PANDA";
  public static final String FILE_PATH = "BEAR";
  public static final Long FILE_SIZE = 500L;
  public static final String ORIGINAL_FILE_NAME = "PANDAPANDA";

  public static Image createImage() {
    return Image.builder()
      .fileName(FILE_NAME)
      .filePath(FILE_PATH)
      .fileSize(FILE_SIZE)
      .originalFileName(ORIGINAL_FILE_NAME)
      .build();
  }
}
