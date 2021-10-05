package com.woomoolmarket.service.member.dto.response;

import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.member.entity.Address;
import com.woomoolmarket.domain.member.entity.AuthProvider;
import com.woomoolmarket.domain.member.entity.Authority;
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

    private LocalDateTime createdDateTime;
    private LocalDateTime lastModifiedDateTime;
    private LocalDateTime leaveDate;

    private Authority authority;
    private Address address;
    private AuthProvider authProvider;
    private Status status;

}
