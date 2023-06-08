package com.pyonsnalcolor.batch.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Builder
@ToString
@Getter
@Document(collection = "event_product")
public class BaseEventProduct {
//    @Transient
//    private static final String SEQUENCE_NAME = "event_sequence";

    @Id
    private String id;
    @Indexed
    private StoreType storeType;
    private String image;
    private EventType eventType;
    @Indexed
    private String name;
    private String price;
    private String originPrice;
    private String giftImage;
    private LocalDateTime updatedTime;
}
