package com.pyonsnalcolor.product.entity;

import com.pyonsnalcolor.product.dto.PbProductResponseDto;
import com.pyonsnalcolor.product.enumtype.Curation;
import com.pyonsnalcolor.product.enumtype.Recommend;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

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
        return PbProductResponseDto.builder()
                .id(getId())
                .name(getName())
                .description(getDescription())
                .image(getImage())
                .storeType(getStoreType())
                .price(formattingPrice(getPrice()))
                .eventType(getEventType())
                .updatedTime(getUpdatedTime())
                .category((getCategory() == null) ? null : getCategory().getKorean())
                .recommend((getRecommend() == null) ? null : getRecommend().getKorean())
                .isNew(getIsNew())
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