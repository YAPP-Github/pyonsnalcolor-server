package com.pyonsnalcolor.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProductFilterRequestDto {
    private List<Integer> filterList;
}