package com.woomoolmarket.service.member.dto.response;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.member.entity.Address;
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

    private String email;
    private String nickname;

    private String profileImage;
    private String phone;
    private String license;

    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private LocalDateTime leaveDate;

    private Address address;
    private AuthProvider authProvider;
    private Status memberStatus;

}
