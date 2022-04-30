package com.woomoolmarket.service.image;

import com.tinify.Tinify;
import com.woomoolmarket.util.constants.ExceptionMessages;
import java.io.File;
import java.io.IOException;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.ExceptionConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class ImageCompressor {

  @Value("${tinify.apiKey}")
  private String apiKey;

  @PostConstruct
  private void setup() {
    Tinify.setKey(apiKey);
  }

  public void compressAndSave(String path, MultipartFile multipartFile) {
    try {
      byte[] buffer = multipartFile.getBytes();
      Tinify.fromBuffer(buffer).toFile(path);

      File file = new File(path);
      file.setWritable(true);
      file.setReadable(true);
    } catch (IOException e) {
      log.info("[WOOMOOL_ERROR] :: Can't Write or Transfer a File => {}", e.getMessage());
      throw new IllegalArgumentException(ExceptionMessages.Image.CANNOT_TRANSFER);
    }
  }
}
