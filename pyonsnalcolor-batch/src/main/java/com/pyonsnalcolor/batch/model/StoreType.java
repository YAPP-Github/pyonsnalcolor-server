package com.pyonsnalcolor.batch.model;

import lombok.Getter;

@Getter
public enum StoreType {
    SEVEN_ELEVEN("seven eleven"),
    CU("cu"),
    GS25("gs25"),
    EMART24("emart24");

    private String name;

    StoreType(String name) {
        this.name = name;
    }
}
