package com.woomoolmarket.service.member.dto.request;

import com.woomoolmarket.domain.member.entity.Address;
import java.io.Serializable;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpMemberRequest implements Serializable {

    @Email
    @NotBlank
    @Size(min = 9, max = 64)
    @Pattern(regexp = "(?i)^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$")
    private String email;

    @NotBlank
    @Pattern(regexp = "^[\\w]{4,24}$")
    private String nickname;

    @NotBlank
    @Pattern(regexp = "^[\\w]{6,24}$")
    private String password;

    @Pattern(regexp = "^[가-힣a-zA-Z ]{10,200}$")
    private Address address;
}

/*
@NotNull    -> null 만 허용 안 함, "", " " 허용
@NotEmpty   -> null, "" 둘다 허용 x, " " 허용
@NotBlank   -> 다 허용 안 함
이 규칙에 따라서 Validation 적용
 */