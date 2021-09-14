package com.woomoolmarket.service.member.dto.response;

import com.woomoolmarket.domain.member.entity.Address;
import com.woomoolmarket.domain.member.entity.MemberStatus;
import com.woomoolmarket.domain.member.entity.Social;
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
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class MemberResponse implements Serializable {

    private Long id;

    private String userId;
    private String email;
    private String nickname;

    private int age;

    private String profileImage;
    private String phone;
    private String license;

    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private LocalDateTime leaveDate;

    private Address address;
    private Social social;
    private MemberStatus memberStatus;

}
