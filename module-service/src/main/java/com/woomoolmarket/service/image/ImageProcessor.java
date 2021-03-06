package com.woomoolmarket.service.image;

import com.woomoolmarket.domain.entity.Image;
import com.woomoolmarket.util.constants.ExceptionMessages;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageProcessor {

  private final Executor woomoolTaskExecutor;
  private final ImageCompressor imageCompressor;

  @Value("${custom.file.path}")
  private String ROOT_PATH;

  public Image parse(MultipartFile file) {
    if (file == null) {
      log.info("[WOOMOOL-INFO] :: There is no files");
      return null;
    }
    List<Image> images = parse(List.of(file));
    return images.get(0);
  }

  public List<Image> parse(List<MultipartFile> files) {
    if (CollectionUtils.isEmpty(files)) {
      log.info("[WOOMOOL-INFO] :: There is no files");
      return Collections.emptyList();
    }

    List<Image> imageList = new ArrayList<>();

    String currentDateString = getCurrentDateString();
    String path = ROOT_PATH + File.separator + currentDateString;
    File file = new File(path);

    checkFileExistence(file);

    for (MultipartFile multipartFile : files) {

      String originalFilename = multipartFile.getOriginalFilename();
      if (!StringUtils.hasText(originalFilename)) {
        log.info("[WOOMOOL-ERROR] :: There is no files => {}", originalFilename);
        continue;
      }

      String contentType = multipartFile.getContentType();
      checkFileExtension(originalFilename, Objects.requireNonNull(contentType));

      String currentTimeString = getCurrentTimeString();
      String fileNameWithoutExtension = originalFilename.substring(0, originalFilename.indexOf("."));
      String convertedFileExtension = getFileExtension(Objects.requireNonNull(contentType));

      String fileName = currentTimeString + fileNameWithoutExtension + convertedFileExtension;

      Image image = Image.builder()
        .originalFileName(originalFilename)
        .fileName(fileName)
        .filePath(File.separator + currentDateString)
        .fileSize(multipartFile.getSize())
        .build();
      imageList.add(image);

      String pathname = path + File.separator + fileName;
      saveFile(pathname, multipartFile);
    }

    return imageList;
  }

  private void saveFile(String path, MultipartFile multipartFile) {
    CompletableFuture.runAsync(
      () -> imageCompressor.compressAndSave(path, multipartFile), woomoolTaskExecutor);
  }

  private void checkFileExistence(File file) {
    if (!file.exists()) {
      boolean wasSuccessful = file.mkdirs();

      if (!wasSuccessful) {
        log.info("[WOOMOOL-ERROR] :: Can't Create a directory");
        throw new IllegalStateException(ExceptionMessages.Image.FOLDER_NOT_FOUND);
      }
    }
  }

  private void checkFileExtension(String originalFilename, String contentType) {
    if (!StringUtils.hasText(contentType)) {
      log.info("[WOOMOOL-ERROR] :: {} has no file extension", originalFilename);
      throw new IllegalArgumentException(ExceptionMessages.Image.NOT_PROPER_EXTENSION);
    }
  }

  private String getFileExtension(String contentType) {
    if (contentType.contains("image/jpeg")) {
      return ".jpg";
    } else if (contentType.contains("image/png")) {
      return ".png";
    } else {
      log.info("[WOOMOOL-ERROR] :: {} is unsupported file extension", contentType);
      throw new IllegalArgumentException(ExceptionMessages.Image.NOT_PROPER_EXTENSION);
    }
  }

  private String getCurrentDateString() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    return LocalDateTime.now().format(formatter);
  }

  private String getCurrentTimeString() {
    return String.valueOf(System.nanoTime());
  }
}