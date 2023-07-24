package com.pyonsnalcolor.product.metadata;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FilterItems {

    private String filterType;
    private List<FilterItem> filterItem;
}