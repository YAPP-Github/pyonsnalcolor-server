package com.pyonsnalcolor.promotion.entity;

import com.pyonsnalcolor.product.enumtype.StoreType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Builder
@ToString
@Getter
@NoArgsConstructor
@Document(collection = "promotion")
public class Promotion {
    @Id
    private String id;
    @Indexed
    private StoreType storeType;
    private String thumbnailImage;
    private String image;
    private String title;
    private LocalDateTime updatedTime;
}
