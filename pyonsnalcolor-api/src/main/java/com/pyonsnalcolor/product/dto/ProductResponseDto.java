package com.pyonsnalcolor.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.pyonsnalcolor.product.enumtype.EventType;
import com.pyonsnalcolor.product.enumtype.StoreType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Schema(description = "상품 조회 Response DTO")
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
    private EventType eventType;
    @NotBlank
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedTime;
    private String description;
    @NotBlank
    private Boolean isNew = false;
}
