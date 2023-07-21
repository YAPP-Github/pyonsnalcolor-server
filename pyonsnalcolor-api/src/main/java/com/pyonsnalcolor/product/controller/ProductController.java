package com.pyonsnalcolor.product.controller;

import com.pyonsnalcolor.product.dto.ProductMetaDataDto;
import com.pyonsnalcolor.product.dto.ProductMetaDataResponseDto;
import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.enumtype.Category;
import com.pyonsnalcolor.product.enumtype.EventType;
import com.pyonsnalcolor.product.enumtype.Sorted;
import com.pyonsnalcolor.product.enumtype.Tag;
import com.pyonsnalcolor.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@io.swagger.v3.oas.annotations.tags.Tag(name = "상품 api")
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 검색", description = "전체 상품 중에 해당 키워드가 포함된 상품을 조회합니다.")
    @GetMapping("/products/search")
    public ResponseEntity<Page<ProductResponseDto>> searchProducts(
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam String name
    ) {
        Page<ProductResponseDto> products = productService.searchProduct(pageNumber, pageSize, name);
        return new ResponseEntity(products, HttpStatus.OK);
    }

    @Operation(summary = "상품 정렬 키값 조회", description = "정렬 관련 키값을 조회합니다.")
    @GetMapping("/products/meta-data")
    public ResponseEntity<ProductMetaDataDto> getProductsMetaDataList() {

        ProductMetaDataDto productMetaDataDto = ProductMetaDataDto.builder()
                .sortedMeta(Sorted.getSortedWithCodes())
                .tagMetaData(Tag.getTagWithCodes())
                .categoryMetaData(Category.getCategoryWithCodes())
                .eventMetaData(EventType.getEventWithCodes())
                .build();

        ProductMetaDataResponseDto productMetaDataResponseDto = ProductMetaDataResponseDto.builder()
                .metaDataList(productMetaDataDto)
                .build();
        return new ResponseEntity(productMetaDataResponseDto, HttpStatus.OK);
    }
}