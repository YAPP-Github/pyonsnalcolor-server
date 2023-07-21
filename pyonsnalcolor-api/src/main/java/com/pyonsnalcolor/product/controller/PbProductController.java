package com.pyonsnalcolor.product.controller;

import com.pyonsnalcolor.product.dto.PbProductResponseDto;
import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.service.PbProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "PB 상품 api")
@RestController
@RequiredArgsConstructor
public class PbProductController {

    private final PbProductService pbProductService;

    @Operation(summary = "PB 상품 조회", description = "PB 상품을 조회합니다.")
    @GetMapping("/products/pb-products")
    public ResponseEntity<Page<ProductResponseDto>> getPbProducts(
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam(defaultValue = "all") String storeType,
            @RequestParam(defaultValue = "updatedTime") String sorted
    ) {
        Page<PbProductResponseDto> products =  pbProductService
                .getProductsWithPaging(pageNumber, pageSize, storeType, sorted);
        return new ResponseEntity(products, HttpStatus.OK);
    }

    @Operation(summary = "PB 상품 단건 조회", description = "id 바탕으로 PB 상품을 조회합니다.")
    @GetMapping("/products/pb-products/{id}")
    public ResponseEntity<ProductResponseDto> getPbProducts(@PathVariable String id) {
        ProductResponseDto product = pbProductService.getProduct(id);
        return new ResponseEntity(product, HttpStatus.OK);
    }
}