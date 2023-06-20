package com.pyonsnalcolor.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
public class NicknameRequestDto {

    @Pattern(regexp="^[0-9a-zA-Zㄱ-ㅎ가-힣 ]{1,15}", message = "닉네임은 특수 문자가 허용되지 않고 15자 이내이어야 합니다.")
    private String nickname;
}
