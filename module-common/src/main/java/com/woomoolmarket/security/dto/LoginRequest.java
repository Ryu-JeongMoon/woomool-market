package com.woomoolmarket.security.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequest {

    @NotBlank
    @Size(min = 9, max = 50)
    private String email;

    @NotBlank
    @Size(min = 4, max = 100)
    private String password;
}
