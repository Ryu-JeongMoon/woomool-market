package com.woomoolmarket.service.image;

import com.tinify.Source;
import com.tinify.Tinify;
import java.io.IOException;
import javax.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ImageCompressor {

    @Value("${tinify.apiKey}")
    private String apiKey;

    @PostConstruct
    private void setup() {
        Tinify.setKey(apiKey);
    }

    public void compressAndSave(byte[] buffer, String path) {
        Source source = Tinify.fromBuffer(buffer);
        try {
            source.toFile(path);
        } catch (IOException e) {
            log.error("[WOOMOOL-FAILED] :: Can't Write a File => {}", e.getCause());
        }
    }
}
