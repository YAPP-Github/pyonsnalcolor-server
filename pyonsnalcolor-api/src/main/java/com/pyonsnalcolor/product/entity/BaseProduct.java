package com.pyonsnalcolor.product.entity;

import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.enumtype.StoreType;
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
    private String description;

    public ProductResponseDto convertToDto() {
        return ProductResponseDto.builder()
                .id(id)
                .name(name)
                .description(description)
                .image(image)
                .storeType(storeType)
                .price(price)
                .updatedTime(updatedTime)
                .isNew(true)
                .build();
    }
}
