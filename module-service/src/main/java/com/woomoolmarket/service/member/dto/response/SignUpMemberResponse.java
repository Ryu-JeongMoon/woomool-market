package com.woomoolmarket.service.member.dto.response;

import com.woomoolmarket.domain.member.entity.Address;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpMemberResponse {

    private Long id;
    private String email;
    private String nickname;
    private Address address;

}
