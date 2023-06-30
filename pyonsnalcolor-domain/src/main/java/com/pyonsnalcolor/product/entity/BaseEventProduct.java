package com.pyonsnalcolor.product.entity;

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
}
