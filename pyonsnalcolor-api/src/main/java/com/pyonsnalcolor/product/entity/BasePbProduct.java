package com.pyonsnalcolor.product.entity;

import com.pyonsnalcolor.product.dto.PbProductResponseDto;
import com.pyonsnalcolor.product.dto.ProductResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@SuperBuilder
@ToString(callSuper = true)
@Getter
@NoArgsConstructor
@Document(collection = "pb_product")
public class BasePbProduct extends BaseProduct {
    @Override
    public ProductResponseDto convertToDto() {
        return PbProductResponseDto.builder()
                .id(getId())
                .name(getName())
                .description(getDescription())
                .image(getImage())
                .storeType(getStoreType())
                .price(getPrice())
                .updatedTime(getUpdatedTime())
                .isNew(true)
                .build();
    }
}
