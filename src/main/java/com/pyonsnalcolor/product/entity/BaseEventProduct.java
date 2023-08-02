package com.pyonsnalcolor.product.entity;

import com.pyonsnalcolor.product.dto.EventProductResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

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
                .originPrice(formattingPrice(getOriginPrice()))
                .giftImage(getGiftImage())
                .giftPrice(formattingPrice(getGiftPrice()))
                .giftTitle(getGiftTitle())
                .isNew(false) // TODO: 2차 배포 이후 행사 상품의 isNew 필드 삭제
                .build();
    }

}