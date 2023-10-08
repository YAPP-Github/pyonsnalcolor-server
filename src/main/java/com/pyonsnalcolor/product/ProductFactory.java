package com.pyonsnalcolor.product;

import com.pyonsnalcolor.product.enumtype.ProductType;
import com.pyonsnalcolor.product.service.EventProductService;
import com.pyonsnalcolor.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductFactory {

    @Autowired
    private ProductService pbProductService;
    @Autowired
    private EventProductService eventProductService;

    public ProductService getService(ProductType productType) {
        switch (productType) {
            case PB:
                return pbProductService;
            case EVENT:
                return eventProductService;
            default:
                throw new IllegalArgumentException("PB 혹은 EVENT 타입이 아닙니다.");
        }
    }
}
