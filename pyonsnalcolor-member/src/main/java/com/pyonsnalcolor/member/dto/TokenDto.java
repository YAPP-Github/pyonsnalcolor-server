package com.pyonsnalcolor.member.dto;

import lombok.*;

@Getter
@Builder
public class TokenDto {

    private String accessToken;

    private String refreshToken;
}
