package com.pyonsnalcolor.push.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushKeywordRequestDto {

    @Schema(description = "저장할 푸시 알림 키워드 이름")
    @Pattern(regexp="^[0-9a-zA-Zㄱ-ㅎ가-힣]{1,10}", message = "공백과 특수 문자 제외 10자 이내이어야 합니다.")
    @NotBlank
    private String name;
}