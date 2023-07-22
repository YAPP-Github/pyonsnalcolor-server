package com.pyonsnalcolor.promotion.entity;

import com.pyonsnalcolor.product.enumtype.StoreType;
import com.pyonsnalcolor.promotion.dto.PromotionResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Builder
@ToString
@Getter
@Document(collection = "promotion")
public class Promotion {
    @Id
    private String id;
    @Indexed
    private StoreType storeType;
    private String thumbnailImage;
    private String image;
    private String title;
    private LocalDateTime updatedTime;

    public PromotionResponseDto convertToDto() {
        PromotionResponseDto promotionResponseDto = new PromotionResponseDto();
        promotionResponseDto.setId(id);
        promotionResponseDto.setStoreType(storeType);
        promotionResponseDto.setThumbnailImage(thumbnailImage);
        promotionResponseDto.setImage(image);
        promotionResponseDto.setTitle(title);
        promotionResponseDto.setUpdatedTime(updatedTime);

        return promotionResponseDto;
    }

    public void updateImage(String image) {
        this.image = image;
    }
}
