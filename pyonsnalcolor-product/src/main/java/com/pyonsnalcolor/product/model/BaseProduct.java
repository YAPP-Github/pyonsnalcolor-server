package com.pyonsnalcolor.product.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@SuperBuilder
@ToString
@Getter
@NoArgsConstructor
public class BaseProduct {
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
