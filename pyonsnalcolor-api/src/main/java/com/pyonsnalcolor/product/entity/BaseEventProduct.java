package com.pyonsnalcolor.product.entity;

import com.pyonsnalcolor.product.dto.EventProductResponseDto;
import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.enumtype.EventType;
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
    private EventType eventType;
    private String originPrice;
    private String giftImage;

    @Override
    public ProductResponseDto convertToDto() {
        return EventProductResponseDto.builder()
                .id(getId())
                .name(getName())
                .description(getDescription())
                .image(getImage())
                .storeType(getStoreType())
                .price(getPrice())
                .updatedTime(getUpdatedTime())
                .eventType(getEventType())
                .originPrice(getOriginPrice())
                .giftImage(getGiftImage())
                .isNew(true)
                .build();
    }
}
