package com.pyonsnalcolor.push.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Schema(description = "푸시 키워드 목록 Response DTO")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushKeywordResponseDto {

    @Schema(description = "푸시 알림 키워드 id")
    @NotBlank
    private Long id;

    @Schema(description = "푸시 알림 키워드 이름")
    @NotBlank
    private String name;
}