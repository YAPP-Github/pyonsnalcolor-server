package com.pyonsnalcolor.push.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Schema(description = "편의점,상품별 구독목록 현황 Response DTO")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushProductStoreResponseDto {

    @Schema(description = "편의점, 상품별 이름", example = "EVENT_CU")
    @NotBlank
    private String productStore;

    @Schema(description = "구독 여부")
    @NotBlank
    private Boolean isSubscribed;
}