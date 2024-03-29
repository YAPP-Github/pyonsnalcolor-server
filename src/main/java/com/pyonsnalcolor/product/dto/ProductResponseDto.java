package com.pyonsnalcolor.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.pyonsnalcolor.product.enumtype.EventType;
import com.pyonsnalcolor.product.enumtype.ProductType;
import com.pyonsnalcolor.product.enumtype.StoreType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@SuperBuilder
@ToString
@Getter
@Setter
@NoArgsConstructor
public class ProductResponseDto {
    @NotBlank
    private String id;
    @NotBlank
    private StoreType storeType;
    @NotBlank
    private String image;
    @NotBlank
    private String name;
    @NotBlank
    private String price;
    @NotBlank
    private EventType eventType;
    @NotBlank
    private ProductType productType;
    @NotBlank
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedTime;
    private String description;
    @NotBlank
    private Boolean isNew;
    @NotBlank
    private int viewCount;
    private String category;
    private Boolean isFavorite;

    public void setFavoriteTrue() {
        this.isFavorite = true;
    }
}
