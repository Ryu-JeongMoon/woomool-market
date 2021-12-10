package com.woomoolmarket.service.image;

import com.woomoolmarket.common.constant.ExceptionConstants;
import com.woomoolmarket.domain.image.entity.Image;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Component
@RequiredArgsConstructor
public class ImageProcessor {

    private final Executor woomoolTaskExecutor;
    private final ImageCompressor imageCompressor;

    @Value("${custom.file.path}")
    private String ROOT_PATH;

    public List<Image> parse(List<MultipartFile> files) {
        if (CollectionUtils.isEmpty(files)) {
            log.warn("[WOOMOOL-FAILED] :: There is no files");
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
                log.warn("[WOOMOOL-FAILED] :: There is no files");
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
        CompletableFuture.runAsync(() -> {
            try {
                imageCompressor.compressAndSave(multipartFile.getBytes(), path);
                File file = new File(path);
                file.setWritable(true);
                file.setReadable(true);
            } catch (IOException e) {
                log.error("[WOOMOOL-FAILED] :: Can't Transfer a file");
                throw new IllegalStateException(ExceptionConstants.IMAGE_CANNOT_TRANSFER);
            }
        }, woomoolTaskExecutor);
    }

    private void checkFileExistence(File file) {
        if (!file.exists()) {
            boolean wasSuccessful = file.mkdirs();

            if (!wasSuccessful) {
                log.error("[WOOMOOL-FAILED] :: Can't Create a directory");
                throw new IllegalStateException(ExceptionConstants.IMAGE_FOLDER_NOT_FOUND);
            }
        }
    }

    private void checkFileExtension(String originalFilename, String contentType) {
        if (!StringUtils.hasText(contentType)) {
            log.error("[WOOMOOL-FAILED] :: {} has no file extension", originalFilename);
            throw new IllegalArgumentException(ExceptionConstants.IMAGE_NOT_PROPER_EXTENSION);
        }
    }

    private String getFileExtension(String contentType) {
        if (contentType.contains("image/jpeg")) {
            return ".jpg";
        } else if (contentType.contains("image/png")) {
            return ".png";
        } else {
            log.error("[WOOMOOL-FAILED] :: {} is unsupported file extension", contentType);
            throw new IllegalArgumentException(ExceptionConstants.IMAGE_NOT_PROPER_EXTENSION);
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