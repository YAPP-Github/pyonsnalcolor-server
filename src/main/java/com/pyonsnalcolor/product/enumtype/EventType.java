package com.pyonsnalcolor.product.enumtype;

import com.pyonsnalcolor.product.metadata.FilterItem;
import com.pyonsnalcolor.product.metadata.FilterItems;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum EventType implements Filter {
    ONE_TO_ONE(10001),
    TWO_TO_ONE(10002),
    GIFT(10003),
    DISCOUNT(10004),
    NONE(10005);

    private final int code;

    private static final String FILTER_TYPE = "event";

    EventType(int code) {
        this.code = code;
    }

    private static List<FilterItem> getFilterItem() {
        return Arrays.stream(EventType.values())
                .filter(s -> !s.equals(EventType.NONE))
                .map(filter -> FilterItem.builder()
                        .name(filter.getKorean())
                        .code(filter.getCode())
                        .build())
                .collect(Collectors.toList());
    }

    public static FilterItems getMetaData() {
        return new FilterItems(FILTER_TYPE, getFilterItem());
    }

    @Override
    public String getKorean() {
        return name();
    }

    @Override
    public String getFilterType() {
        return FILTER_TYPE;
    }

    @Override
    public List<String> getKeywords() {
        return null; // 필요X
    }
}