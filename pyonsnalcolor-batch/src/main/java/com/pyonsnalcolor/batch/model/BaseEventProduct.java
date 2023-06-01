package com.pyonsnalcolor.batch.model;

import lombok.Builder;
import lombok.ToString;

import java.time.LocalDateTime;


@Builder
@ToString
public class BaseEventProduct {
    private Long id;
    private String storeType;
    private String image;
    private EventType eventType;
    private String name;
    private String price;
    private String originPrice;
    private String giftImage;
    private LocalDateTime updatedTime;
}
