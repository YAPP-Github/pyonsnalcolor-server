package com.pyonsnalcolor.domain.product.enumtype;

import lombok.Getter;

import static com.pyonsnalcolor.domain.product.enumtype.ProductType.*;
import static com.pyonsnalcolor.domain.product.enumtype.StoreType.*;

@Getter
public enum ProductStoreType {
    PB_GS25(GS25, PB),
    PB_CU(CU, PB),
    PB_EMART24(EMART24, PB),
    PB_SEVEN_ELEVEN(SEVEN_ELEVEN, PB),
    EVENT_GS25(GS25, EVENT),
    EVENT_CU(CU, EVENT),
    EVENT_EMART24(EMART24, EVENT),
    EVENT_SEVEN_ELEVEN(SEVEN_ELEVEN, EVENT);

    private final StoreType storeType;
    private final ProductType productType;

    ProductStoreType(StoreType storeType, ProductType productType) {
        this.storeType = storeType;
        this.productType = productType;
    }
}