package com.pyonsnalcolor.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Schema(description = "Pb 상품 조회 Response DTO")
@SuperBuilder
@ToString(callSuper = true)
@Getter
@NoArgsConstructor
public class PbProductResponseDto extends ProductResponseDto {
}
