package com.pyonsnalcolor.product.metadata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FilterItem {

    private String name;
    private int code;
}