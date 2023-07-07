package com.pyonsnalcolor.push.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Schema(description = "푸시 구독 신청/취소용 Request DTO")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushProductStoreRequestDto {

    @Schema(description = "상품종류_편의점명 리스트", example = "{ \"pushProductStores\": [\"PB_GS25\", \"EVENT_CU\"]}")
    @NotBlank
    private List<String> productStores;
}