package com.pyonsnalcolor.member.oauth.kakao.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoUserInfoDto {

    private KakaoAccount kakaoAccount;

    static class KakaoAccount {
        private String email;
    }

    public String getEmail() {
        return kakaoAccount.email;
    }
}