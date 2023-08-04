package com.pyonsnalcolor.promotion.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.pyonsnalcolor.product.enumtype.StoreType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Schema(description = "이벤트 상품 조회 Response DTO")
@ToString(callSuper = true)
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromotionResponseDto {
    @NotBlank
    private String id;
    @NotBlank
    private StoreType storeType;
    @NotBlank
    private String thumbnailImage;
    @NotBlank
    private String image;
    private String title;
    @NotBlank
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedTime;
}
