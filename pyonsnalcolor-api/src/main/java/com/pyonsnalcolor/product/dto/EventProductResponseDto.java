package com.pyonsnalcolor.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;


@Schema(description = "이벤트 상품 조회 Response DTO")
@SuperBuilder
@ToString(callSuper = true)
@Getter
@NoArgsConstructor
public class EventProductResponseDto extends ProductResponseDto {
    @NotBlank
    private String originPrice;
    @NotBlank
    private String giftImage;
}
