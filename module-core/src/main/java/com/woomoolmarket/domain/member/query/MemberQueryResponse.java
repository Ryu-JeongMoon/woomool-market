package com.woomoolmarket.domain.member.query;

import com.woomoolmarket.common.embeddable.Address;
import com.woomoolmarket.common.enumeration.Status;
import com.woomoolmarket.domain.member.entity.AuthProvider;
import com.woomoolmarket.domain.member.entity.Authority;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberQueryResponse implements Serializable {

    private Long id;

    private String email;
    private String nickname;

    private String profileImage;
    private String phone;
    private String license;

    private LocalDateTime createdDateTime;
    private LocalDateTime lastModifiedDateTime;
    private LocalDateTime leaveDateTime;

    private Authority authority;
    private Address address;
    private AuthProvider authProvider;
    private Status status;

    public static MemberQueryResponse from(String email) {
        return MemberQueryResponse.builder()
            .email(email)
            .build();
    }
}
