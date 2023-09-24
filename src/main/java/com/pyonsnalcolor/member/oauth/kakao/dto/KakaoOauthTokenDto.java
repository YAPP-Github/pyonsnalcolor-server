package com.pyonsnalcolor.member.oauth.kakao.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
public class KakaoOauthTokenDto {

    private String accessToken;

    private String tokenType;

    private String refreshToken;

    private String expiresIn;

    private String refreshTokenExpiresIn;

    private String scope;
}