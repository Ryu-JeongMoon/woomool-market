package com.woomoolmarket.helper;

import java.nio.charset.StandardCharsets;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MultipartFileTestHelper {

  public static final String DATA = "panda panda panda";
  public static final String NAME = "files";
  public static final String ORIGINAL_FILENAME = "hehe.jpg";
  public static final String CONTENT_TYPE = "image/jpeg";

  public MultipartFile createMultipartFile() {
    return new MockMultipartFile(
      NAME,
      ORIGINAL_FILENAME,
      CONTENT_TYPE,
      DATA.getBytes(StandardCharsets.UTF_8));
  }
}
