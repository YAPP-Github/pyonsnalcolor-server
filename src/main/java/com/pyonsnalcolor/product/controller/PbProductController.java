package com.pyonsnalcolor.product.controller;

import com.pyonsnalcolor.product.dto.PbProductResponseDto;
import com.pyonsnalcolor.product.dto.ProductFilterRequestDto;
import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.dto.ReviewDto;
import com.pyonsnalcolor.product.entity.Review;
import com.pyonsnalcolor.product.service.PbProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            @RequestParam String storeType,
            @RequestBody ProductFilterRequestDto productFilterRequestDto
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ProductResponseDto> products =  pbProductService.getPagedProductsDtoByFilter(
                pageable, storeType, productFilterRequestDto);
        return new ResponseEntity(products, HttpStatus.OK);
    }

    @Operation(summary = "리뷰 등록", description = "특정 상품의 리뷰를 등록합니다")
    @PostMapping(value = "/products/pb-products/{id}/reviews",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> registerReview(
            @PathVariable String id,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestPart(value = "reviewDto") ReviewDto reviewDto
    ) throws Throwable {
        pbProductService.registerReview(imageFile, reviewDto, id);

        return ResponseEntity.noContent().build(); //현재는 리뷰 단건 조회하는 기능 존재 x -> location 지정 안함
    }

    @Operation(summary = "PB 상품 단건 조회", description = "id로 PB 상품을 조회합니다.")
    @GetMapping("/products/pb-products/{id}")
    public ResponseEntity<PbProductResponseDto> getPbProduct(@PathVariable String id) {
        ProductResponseDto product = pbProductService.getProductById(id);
        return new ResponseEntity(product, HttpStatus.OK);
    }
}
