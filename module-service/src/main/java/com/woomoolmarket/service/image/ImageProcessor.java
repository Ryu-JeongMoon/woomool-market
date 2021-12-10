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
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@Component
public class ImageProcessor {

    @Value("${custom.file.path}")
    private String PATH;

    public List<Image> parse(List<MultipartFile> files) {
        if (CollectionUtils.isEmpty(files) || files == null) {
            log.warn("FAILED :: There is no files");
            return Collections.emptyList();
        }

        List<Image> imageList = new ArrayList<>();

        String currentDateString = getCurrentDateString();
        String path = PATH + File.separator + currentDateString;
        File file = new File(path);

        checkFileExistence(file);

        for (MultipartFile multipartFile : files) {
            String originalFilename = multipartFile.getOriginalFilename();

            if (!StringUtils.hasText(originalFilename)) {
                log.warn("FAILED :: There is no files");
                continue;
            }

            String currentTimeString = getCurrentTimeString();
            String contentType = multipartFile.getContentType();
            String fileNameWithoutExtension = originalFilename.substring(0, originalFilename.indexOf("."));

            checkFileExtension(multipartFile, contentType);

            String convertedFileExtension = getFileExtension(contentType);
            String fileName = currentTimeString + fileNameWithoutExtension + convertedFileExtension;

            Image image = Image.builder()
                .originalFileName(originalFilename)
                .fileName(fileName)
                .filePath(File.separator + currentDateString)
                .fileSize(multipartFile.getSize())
                .build();

            imageList.add(image);

            file = new File(path + File.separator + fileName);

            try {
                multipartFile.transferTo(file);
            } catch (IOException e) {
                log.error("FAILED :: Can't Transfer a file");
                throw new IllegalStateException(ExceptionConstants.IMAGE_CANNOT_TRANSFER);
            }

            file.setWritable(true);
            file.setReadable(true);
        }

        return imageList;
    }

    private void checkFileExistence(File file) {
        if (!file.exists()) {
            boolean wasSuccessful = file.mkdirs();

            if (!wasSuccessful) {
                log.error("FAILED :: Can't Create a directory");
                throw new IllegalStateException(ExceptionConstants.IMAGE_FOLDER_NOT_FOUND);
            }
        }
    }

    private void checkFileExtension(MultipartFile multipartFile, String contentType) {
        if (!StringUtils.hasText(contentType)) {
            log.error("FAILED :: {} has no file extension", multipartFile.getOriginalFilename());
            throw new IllegalArgumentException(ExceptionConstants.IMAGE_NOT_PROPER_EXTENSION);
        }
    }

    private String getFileExtension(String contentType) {
        if (contentType.contains("image/jpeg")) {
            return ".jpg";
        } else if (contentType.contains("image/png")) {
            return ".png";
        } else {
            log.error("FAILED :: {} is unsupported file extension", contentType);
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