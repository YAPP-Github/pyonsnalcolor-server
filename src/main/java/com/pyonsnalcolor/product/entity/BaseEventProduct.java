package com.pyonsnalcolor.product.entity;

import com.pyonsnalcolor.product.dto.EventProductResponseDto;
import com.pyonsnalcolor.product.dto.ReviewDto;
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
@Document(collection = "event_product")
public class BaseEventProduct extends BaseProduct {
    private Integer originPrice;
    private String giftImage;
    private String giftTitle;
    private Integer giftPrice;

    @Override
    public EventProductResponseDto convertToDto() {
        List<ReviewDto> reviewDtos = new ArrayList<>();
        float avgScore = 0;

        for (Review review : getReviews()) {
            reviewDtos.add(review.convertToDto());
            avgScore += review.getScore();
        }
        avgScore = avgScore / reviewDtos.size();

        return EventProductResponseDto.builder()
                .id(getId())
                .name(getName())
                .description(getDescription())
                .image(getImage())
                .storeType(getStoreType())
                .price(formattingPrice(getPrice()))
                .updatedTime(getCreatedDate())
                .eventType(getEventType())
                .category((getCategory() == null) ? null : getCategory().getKorean())
                .originPrice(getOriginPrice() == null ? null : formattingPrice(getOriginPrice()))
                .giftImage(getGiftImage())
                .giftPrice(getGiftPrice() == null ? null : formattingPrice(getGiftPrice()))
                .giftTitle(getGiftTitle())
                .isNew(false) // TODO: 2차 배포 이후 행사 상품의 isNew 필드 삭제
                .viewCount(getViewCount())
                .reviews(reviewDtos)
                .avgScore(avgScore)
                .build();
    }
}
