package com.woomoolmarket.helper;

import java.nio.charset.StandardCharsets;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MultipartFileTestHelper {

    private static final String DATA = "panda panda panda";
    private static final String NAME = "files";
    private static final String ORIGINAL_FILENAME = "hehe.jpg";
    private static final String CONTENT_TYPE = "image/jpeg";

    public MultipartFile createMultipartFile() {
        return new MockMultipartFile(
            NAME,
            ORIGINAL_FILENAME,
            CONTENT_TYPE,
            DATA.getBytes(StandardCharsets.UTF_8));
    }
}
