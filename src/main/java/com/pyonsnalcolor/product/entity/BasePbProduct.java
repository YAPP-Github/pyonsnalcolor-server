package com.pyonsnalcolor.product.entity;

import com.pyonsnalcolor.product.dto.PbProductResponseDto;
import com.pyonsnalcolor.product.dto.ReviewDto;
import com.pyonsnalcolor.product.enumtype.Curation;
import com.pyonsnalcolor.product.enumtype.EventType;
import com.pyonsnalcolor.product.enumtype.ProductType;
import com.pyonsnalcolor.product.enumtype.Recommend;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@ToString(callSuper = true)
@Getter
@NoArgsConstructor
@Document(collection = "pb_product")
public class BasePbProduct extends BaseProduct {
    private Recommend recommend;
    private Curation curation;

    @Override
    public PbProductResponseDto convertToDto() {
        List<ReviewDto> reviewDtos = new ArrayList<>();
        float avgScore = 0;

        for (Review review : getReviews()) {
            reviewDtos.add(review.convertToDto());
            avgScore += review.getScore();
        }
        if(reviewDtos.size() > 0) {
            avgScore = avgScore / reviewDtos.size();
        }

        return PbProductResponseDto.builder()
                .id(getId())
                .name(getName())
                .description(getDescription())
                .image(getImage())
                .storeType(getStoreType())
                .price(formattingPrice(getPrice()))
                .eventType(getEventType() == null ? EventType.NONE : getEventType())
                .productType(ProductType.PB)
                .updatedTime(getCreatedDate())
                .category((getCategory() == null) ? null : getCategory().getKorean())
                .recommend((getRecommend() == null) ? null : getRecommend().getKorean())
                .isNew(getIsNew() == null ? false : getIsNew())
                .viewCount(getViewCount())
                .reviews(reviewDtos)
                .avgScore(avgScore)
                .isFavorite(false)
                .build();
    }

    public void updateRecommend(Recommend recommend) {
        this.recommend = recommend;
    }

    public void deleteRecommend() {
        this.recommend = null;
    }

    public void updateCuration(Curation curation) {
        this.curation = curation;
    }

    public void deleteCuration() {
        this.curation = null;
    }
}
