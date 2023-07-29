package com.pyonsnalcolor.product.controller;

import com.pyonsnalcolor.product.metadata.FilterItems;
import com.pyonsnalcolor.product.metadata.ProductMetaData;
import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.service.SearchProduct;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@io.swagger.v3.oas.annotations.tags.Tag(name = "상품 api")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final SearchProduct searchProduct;

    @Operation(summary = "상품 검색", description = "전체 상품 중에 해당 키워드가 포함된 상품을 조회합니다.")
    @GetMapping("/products/search")
    public ResponseEntity<Page<ProductResponseDto>> searchProducts(
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam String name
    ) {
        Page<ProductResponseDto> products = searchProduct.searchProduct(pageNumber, pageSize, name);
        return new ResponseEntity(products, HttpStatus.OK);
    }

    @Operation(summary = "상품 관련 메타 데이터 조회", description = "상품 항목별 메타 데이터를 조회합니다.")
    @GetMapping("/products/meta-data")
    public ResponseEntity<Map<String, List<FilterItems>>> getProductMetaData() {
        ProductMetaData productMetaData = ProductMetaData.getInstance();
        Map<String, List<FilterItems>> result = productMetaData.getMetadata();
        return new ResponseEntity(result, HttpStatus.OK);
    }
}