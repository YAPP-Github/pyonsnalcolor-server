package com.pyonsnalcolor.product.controller;

import com.pyonsnalcolor.product.dto.EventProductResponseDto;
import com.pyonsnalcolor.product.dto.ProductFilterRequestDto;
import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.service.EventProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "이벤트 상품 api")
@RestController
@RequiredArgsConstructor
public class EventProductController {

    private final EventProductService eventProductService;

    @Operation(summary = "이벤트 상품 필터 조회", description = "이벤트 상품을 필터링 조회합니다.")
    @PostMapping("/products/event-products")
    public ResponseEntity<Page<EventProductResponseDto>> getEventProductsByFilter(
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam(defaultValue = "all") String storeType,
            @RequestBody ProductFilterRequestDto productFilterRequestDto
    ) {
        Page<ProductResponseDto> products = eventProductService.getFilteredProducts(
                pageNumber, pageSize, storeType, productFilterRequestDto);
        return new ResponseEntity(products, HttpStatus.OK);
    }

    @Operation(summary = "이벤트 상품 단건 조회", description = "id 바탕으로 이벤트 상품을 조회합니다.")
    @GetMapping("/products/event-products/{id}")
    public ResponseEntity<EventProductResponseDto> getEventProduct(@PathVariable String id) {
        ProductResponseDto responseDto = eventProductService.getProduct(id);
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }
}