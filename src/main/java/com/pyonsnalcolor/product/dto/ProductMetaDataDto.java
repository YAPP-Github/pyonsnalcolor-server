package com.pyonsnalcolor.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ProductMetaDataDto {

    @NotBlank
    private List<MetaDataDto> sortedMeta;
    @NotBlank
    private List<MetaDataDto> tagMetaData;
    @NotBlank
    private List<MetaDataDto> categoryMetaData;
    @NotBlank
    private List<MetaDataDto> eventMetaData;
}