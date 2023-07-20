package com.pyonsnalcolor.product.controller;

import com.pyonsnalcolor.product.dto.ProductMetaDataDto;
import com.pyonsnalcolor.product.dto.ProductMetaDataResponseDto;
import com.pyonsnalcolor.product.enumtype.Category;
import com.pyonsnalcolor.product.enumtype.EventType;
import com.pyonsnalcolor.product.enumtype.Sorted;
import com.pyonsnalcolor.product.enumtype.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@io.swagger.v3.oas.annotations.tags.Tag(name = "상품 메타 데이터 api")
@RestController
@RequiredArgsConstructor
public class ProductMetaDataController {

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