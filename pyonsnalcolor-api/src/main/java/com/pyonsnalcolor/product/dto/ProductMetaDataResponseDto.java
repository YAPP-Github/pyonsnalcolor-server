package com.pyonsnalcolor.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProductMetaDataResponseDto {

    private ProductMetaDataDto metaDataList;
}