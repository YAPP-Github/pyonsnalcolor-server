package com.pyonsnalcolor.promotion.entity;

import com.pyonsnalcolor.product.entity.BaseTimeEntity;
import com.pyonsnalcolor.product.enumtype.StoreType;
import com.pyonsnalcolor.promotion.dto.PromotionResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@ToString
@Getter
@Document(collection = "promotion")
public class Promotion extends BaseTimeEntity {
    @Id
    private String id;
    @Indexed
    private StoreType storeType;
    private String thumbnailImage;
    private String image;
    private String title;

    public PromotionResponseDto convertToDto() {

        return PromotionResponseDto.builder()
                .id(id)
                .image(image)
                .thumbnailImage(thumbnailImage)
                .title(title)
                .storeType(storeType)
                .updatedTime(getCreatedDate())
                .build();
    }
}