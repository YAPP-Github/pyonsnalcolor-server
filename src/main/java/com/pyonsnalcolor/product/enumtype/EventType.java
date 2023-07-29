package com.pyonsnalcolor.product.enumtype;

import lombok.Getter;

import java.util.List;

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