package com.woomoolmarket.service.member.dto.response;

import com.woomoolmarket.common.BaseTimeEntity;
import com.woomoolmarket.entity.member.entity.Address;
import com.woomoolmarket.entity.member.entity.MemberStatus;
import com.woomoolmarket.entity.member.entity.Social;
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
public class MemberResponse extends BaseTimeEntity implements Serializable {

    private Long id;

    private String userId;
    private String email;
    private String nickname;

    private int age;

    private String profileImage;
    private String phone;
    private String license;

    private LocalDateTime leaveDate;

    private Address address;
    private Social social;
    private MemberStatus memberStatus;

}
