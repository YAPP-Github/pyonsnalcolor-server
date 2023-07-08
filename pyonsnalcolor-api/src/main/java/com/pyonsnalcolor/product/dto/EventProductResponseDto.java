package com.pyonsnalcolor.product.dto;

import com.pyonsnalcolor.product.enumtype.EventType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@ToString(callSuper = true)
@Getter
@NoArgsConstructor
public class EventProductResponseDto extends ProductResponseDto {
    private EventType eventType;
    private String originPrice;
    private String giftImage;
}
