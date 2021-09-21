package com.woomoolmarket.service.member.dto.request;

import com.woomoolmarket.domain.member.entity.Address;
import javax.persistence.Embedded;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyMemberRequest {

    @Pattern(regexp = "^[\\w]{4,24}$")
    private String nickname;

    @Pattern(regexp = "^[\\w]{6,24}$")
    private String password;

    @Size(max = 255)
    @Pattern(regexp = "^[\\w]*$")
    private String profileImage;

    @Pattern(regexp = "^01(\\d{8,9})$")
    private String phone;

    @Pattern(regexp = "^(\\d{10})$")
    private String license;

    @Embedded
    @Pattern(regexp = "^[가-힣a-zA-Z ]{10,200}$")
    private Address address;
}
