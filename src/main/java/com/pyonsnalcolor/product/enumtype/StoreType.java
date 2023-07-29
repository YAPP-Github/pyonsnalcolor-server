package com.pyonsnalcolor.product.enumtype;

import lombok.Getter;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Arrays;

@Getter
public enum StoreType {
    SEVEN_ELEVEN, CU, GS25, EMART24, ALL;

    public static final String FILTER_TYPE = "storeType";

    public static StoreType getStoreType(String storeTypeStr) {
        String storeTypeUppercase = storeTypeStr.toUpperCase();
        return Arrays.stream(StoreType.values())
                .filter(s -> s.name().equals(storeTypeUppercase))
                .findFirst()
                .orElseThrow(NoSuchFieldError::new);
    }

    public static Criteria getCriteria(String storeTypeStr, Criteria criteria) {
        StoreType storeType = StoreType.getStoreType(storeTypeStr);
        if (storeType == StoreType.ALL) {
            return criteria;
        }
        return criteria.and(FILTER_TYPE).is(storeType);
    }
}