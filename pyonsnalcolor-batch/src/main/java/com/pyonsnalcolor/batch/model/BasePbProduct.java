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
@Document(collection = "pb_product")
public class BasePbProduct {
//    @Transient
//    private static final String SEQUENCE_NAME = "pb_sequence";

    @Id
    private String id;
    @Indexed
    private StoreType storeType;
    private String image;
    @Indexed
    private String name;
    private String price;
    private LocalDateTime updatedTime;
}
