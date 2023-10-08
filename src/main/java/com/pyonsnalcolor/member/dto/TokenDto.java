package com.pyonsnalcolor.member.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {

    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;
}