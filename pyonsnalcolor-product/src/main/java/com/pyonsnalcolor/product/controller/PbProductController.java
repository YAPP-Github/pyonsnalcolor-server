package com.pyonsnalcolor.product.controller;


import com.pyonsnalcolor.product.model.BasePbProduct;
import com.pyonsnalcolor.product.service.PbProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class PbProductController {
    private final PbProductService pbProductService;

    @GetMapping("/products/pb-products")
    public Page<BasePbProduct> getPbProducts(@RequestParam("pageNumber") int pageNumber,
                                             @RequestParam("pageSize") int pageSize,
                                             @RequestParam(
                                                     value = "storeType",
                                                     defaultValue = "all"
                                             ) String storeType,
                                             @RequestParam(
                                                     value = "sorted",
                                                     defaultValue = "updatedTime"
                                             ) String sorted) {
        return pbProductService.getProductsWithPaging(pageNumber, pageSize, storeType, sorted);
    }
}
