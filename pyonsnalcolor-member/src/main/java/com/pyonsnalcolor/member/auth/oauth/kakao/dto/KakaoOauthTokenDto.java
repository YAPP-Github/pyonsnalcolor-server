package com.pyonsnalcolor.member.auth.oauth.kakao.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoOauthTokenDto {

    private String accessToken;

    private String tokenType;

    private String refreshToken;

    private String expiresIn;

    private String refreshTokenExpiresIn;

    private String scope;
}