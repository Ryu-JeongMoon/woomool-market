package com.woomoolmarket.service.member.dto.request;

import com.woomoolmarket.entity.member.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModifyMemberRequest {

    private String password;

    private String age;

    private String profileImage;

    private String phone;

    private String license;

    @Embedded
    private Address address;

}
