package com.pyonsnalcolor.product.controller;

import com.pyonsnalcolor.product.model.BaseEventProduct;
import com.pyonsnalcolor.product.service.EventProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EventProductController {
    private final EventProductService eventProductService;

    @GetMapping("/products/event-products")
    public Page<BaseEventProduct> getEventProducts(@RequestParam("pageNumber") int pageNumber,
                                                   @RequestParam("pageSize") int pageSize,
                                                   @RequestParam(
                                                           value = "storeType",
                                                           defaultValue = "all"
                                                   ) String storeType,
                                                   @RequestParam(
                                                           value = "sorted",
                                                           defaultValue = "updatedTime"
                                                   ) String sorted) {
        return eventProductService.getProductsWithPaging(pageNumber, pageSize, storeType, sorted);
    }
}
