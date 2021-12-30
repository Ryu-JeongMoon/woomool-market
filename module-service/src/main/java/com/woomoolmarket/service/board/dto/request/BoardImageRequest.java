package com.woomoolmarket.service.board.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BoardImageRequest {

    private String title;

    @Lob
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private String startDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private String endDateTime;

    private List<MultipartFile> files;

    public BoardRequest toBoardRequest() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        return BoardRequest.builder()
            .title(title)
            .content(content)
            .startDateTime(LocalDateTime.parse(startDateTime, formatter))
            .endDateTime(LocalDateTime.parse(endDateTime, formatter))
            .build();
    }

    public BoardRequest toEditNoticeRequest() {
        return BoardRequest.builder()
            .title(title)
            .content(content)
            .build();
    }
}
