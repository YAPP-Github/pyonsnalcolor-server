package com.pyonsnalcolor.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "PICK! 조회 화면의 큐레이션들 전체 Response DTO")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CurationProductsResponseDto {
    @NotNull
    private List<CurationProductResponseDto> curationProducts;
}