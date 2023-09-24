package com.pyonsnalcolor.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Schema(description = "이벤트 상품 조회 Response DTO")
@SuperBuilder
@ToString(callSuper = true)
@Getter
@NoArgsConstructor
public class EventProductResponseDto extends ProductResponseDto {
    private String originPrice;
    private String giftImage;
    private String giftTitle;
    private String giftPrice;
    private Boolean isEventExpired;
    private List<ReviewDto> reviews;
    private float avgScore;
}
