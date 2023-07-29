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
    private String originPrice;
    private String giftImage;
    private String giftTitle;
    private String giftPrice;

    @Override
    public EventProductResponseDto convertToDto() {
        return EventProductResponseDto.builder()
                .id(getId())
                .name(getName())
                .description(getDescription())
                .image(getImage())
                .storeType(getStoreType())
                .price(getPrice())
                .updatedTime(getUpdatedTime())
                .eventType(getEventType())
                .category((getCategory() == null) ? null : getCategory().getKorean())
                .originPrice(getOriginPrice())
                .giftImage(getGiftImage())
                .giftPrice(getGiftPrice())
                .giftTitle(getGiftTitle())
                .isNew(getIsNew())
                .build();
    }
}