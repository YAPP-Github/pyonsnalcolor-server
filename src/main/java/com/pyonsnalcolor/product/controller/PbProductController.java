package com.pyonsnalcolor.product.controller;

import com.pyonsnalcolor.product.dto.PbProductResponseDto;
import com.pyonsnalcolor.product.dto.ProductFilterRequestDto;
import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.service.PbProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "PB 상품 api")
@RestController
@RequiredArgsConstructor
public class PbProductController {

    private final PbProductService pbProductService;

    @Operation(summary = "PB 상품 필터 조회", description = "PB 상품을 필터링 조회합니다.")
    @PostMapping("/products/pb-products")
    public ResponseEntity<Page<PbProductResponseDto>> getPbProductsByFilter(
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam(defaultValue = "all") String storeType,
            @RequestBody ProductFilterRequestDto productFilterRequestDto
    ) {
        Page<ProductResponseDto> products =  pbProductService.getFilteredProducts(
                pageNumber, pageSize, storeType, productFilterRequestDto);
        return new ResponseEntity(products, HttpStatus.OK);
    }

    @Operation(summary = "PB 상품 단건 조회", description = "id 바탕으로 PB 상품을 조회합니다.")
    @GetMapping("/products/pb-products/{id}")
    public ResponseEntity<PbProductResponseDto> getPbProduct(@PathVariable String id) {
        ProductResponseDto product = pbProductService.getProduct(id);
        return new ResponseEntity(product, HttpStatus.OK);
    }
}