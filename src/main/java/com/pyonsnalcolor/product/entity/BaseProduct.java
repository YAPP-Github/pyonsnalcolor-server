package com.pyonsnalcolor.product.entity;

import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.enumtype.Category;
import com.pyonsnalcolor.product.enumtype.EventType;
import com.pyonsnalcolor.product.enumtype.Recommend;
import com.pyonsnalcolor.product.enumtype.StoreType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.persistence.Id;
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
    private String description;
    private Category category;
    private Recommend recommend;
    private EventType eventType; // TODO: 이후 추가
    private boolean isNew; // TODO: 이후 추가

    public ProductResponseDto convertToDto() {
        return ProductResponseDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .image(image)
                .storeType(storeType)
                .price(price)
                .updatedTime(updatedTime)
                .isNew(isNew)
                .category(category)
                .recommend(recommend)
                .build();
    }
}
