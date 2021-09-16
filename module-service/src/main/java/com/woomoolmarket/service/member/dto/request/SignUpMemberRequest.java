package com.woomoolmarket.service.member.dto.request;

import com.woomoolmarket.domain.member.entity.Address;
import java.io.Serializable;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpMemberRequest implements Serializable {

    @Email
    @NotEmpty
    @Size(min = 9, max = 255)
    private String email;

    @NotEmpty
    private String userId;

    @NotEmpty
    private String nickname;

    @Size(min = 6, max = 255)
    @NotEmpty
    private String password;

    private Address address;
}
