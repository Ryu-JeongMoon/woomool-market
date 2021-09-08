package com.woomoolmarket.service.member.dto.response;

import com.woomoolmarket.entity.member.entity.Address;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class SignUpMemberResponse {

    private Long id;
    private String email;
    private String userId;
    private String nickname;
    private Address address;

}
