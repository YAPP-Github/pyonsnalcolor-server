package com.pyonsnalcolor.member.dto;

import com.pyonsnalcolor.product.enumtype.ProductType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "상품 찜하기 등록 DTO")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FavoriteRequestDto {

    private String productId;

    private String productType;

    public ProductType getProductType() {
        return ProductType.valueOf(productType.toUpperCase());
    }
}