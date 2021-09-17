package com.woomoolmarket.service.member.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.woomoolmarket.domain.member.entity.Address;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.member.entity.AuthProvider;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponse implements Serializable {

    private Long id;

    private String userId;
    private String email;
    private String nickname;

    private int age;

    private String profileImage;
    private String phone;
    private String license;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime lastModifiedDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime leaveDate;

    private Address address;
    private AuthProvider social;
    private Status memberStatus;

}
