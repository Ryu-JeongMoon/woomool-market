package com.woomoolmarket.service.member.dto.request;

import com.woomoolmarket.entity.member.entity.Address;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpMemberRequest {

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String userId;

    @NotEmpty
    private String nickname;

    @Min(6)
    @NotEmpty
    private String password;

    private Address address;
}
