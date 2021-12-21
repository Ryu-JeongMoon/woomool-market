package com.woomoolmarket.service.member.dto.request;

import com.woomoolmarket.common.embeddable.Address;
import javax.annotation.Nullable;
import javax.persistence.Embedded;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyRequest {

    @Pattern(regexp = "^[\\w]{4,24}$", message = "닉네임은 4 - 24자 사이로 입력 가능합니다")
    private String nickname;

    @Pattern(regexp = "^[\\w]{4,24}$", message = "비밀번호는 4 - 24자 사이로 입력 가능합니다")
    private String password;

    @Size(max = 255)
    @Pattern(regexp = "^[\\w]*$", message = "파일 이름은 255자까지 입력 가능합니다")
    private String profileImage;

    @Pattern(regexp = "^01(\\d{8,9})$", message = "01X-XXXX-XXXX 또는 01X-XXX-XXXX와 같이 입력 가능합니다")
    private String phone;

    @Pattern(regexp = "^(\\d{10})$", message = "사업자 번호는 10자리만 입력 가능합니다")
    private String license;

    @Embedded
    private Address address;

    @Nullable
    private MultipartFile file;
}
