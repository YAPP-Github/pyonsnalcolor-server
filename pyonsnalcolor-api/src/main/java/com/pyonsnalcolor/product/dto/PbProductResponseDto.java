package com.pyonsnalcolor.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@ToString(callSuper = true)
@Getter
@NoArgsConstructor
public class PbProductResponseDto extends ProductResponseDto {
}
