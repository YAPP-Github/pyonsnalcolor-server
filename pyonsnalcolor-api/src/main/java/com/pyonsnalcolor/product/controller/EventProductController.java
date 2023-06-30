package com.pyonsnalcolor.product.controller;

import com.pyonsnalcolor.product.entity.BaseEventProduct;
import com.pyonsnalcolor.product.service.EventProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "EventProductController", description = "행사 상품 api")
@RestController
@RequiredArgsConstructor
public class EventProductController {
    private final EventProductService eventProductService;

    @Operation(summary = "행사 상품 조회", description = "행사 상품을 조회합니다.")
    @Parameter(name = "pageNumber", description = "조회할 페이지 번호")
    @Parameter(name = "pageSize", description = "페이지별 상품 갯수")
    @Parameter(name = "storeType", description = "편의점 종류, default는 모든 편의점")
    @Parameter(name = "sorted", description = "정렬순서, default는 최신순")
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
