package com.pyonsnalcolor.batch.model;

import lombok.Builder;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@ToString
public class BasePbProduct {
    private Long id;
    private String storeType;
    private String image;
    private String name;
    private String price;
    private LocalDateTime updatedTime;
}
