package com.pyonsnalcolor.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Schema(description = "변경할 닉네임 Request DTO")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NicknameRequestDto {

    @Schema(description = "변경할 닉네임, 특수문자 제외 15자 이내", required = true)
    @Pattern(regexp="^[0-9a-zA-Zㄱ-ㅎ가-힣 ]{1,15}", message = "닉네임은 특수 문자가 허용되지 않고 15자 이내이어야 합니다.")
    private String nickname;
}
