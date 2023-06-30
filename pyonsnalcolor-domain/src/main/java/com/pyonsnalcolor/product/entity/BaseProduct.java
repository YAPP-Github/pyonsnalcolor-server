package com.pyonsnalcolor.product.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedTime;
}
