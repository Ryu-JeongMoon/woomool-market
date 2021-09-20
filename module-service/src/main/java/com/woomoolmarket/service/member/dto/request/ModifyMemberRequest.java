package com.woomoolmarket.service.member.dto.request;

import com.woomoolmarket.domain.member.entity.Address;
import javax.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModifyMemberRequest {

    private String password;

    private String profileImage;

    private String phone;

    private String license;

    @Embedded
    private Address address;

}
