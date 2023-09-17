package com.pyonsnalcolor.product.controller;

import com.pyonsnalcolor.product.dto.EventProductResponseDto;
import com.pyonsnalcolor.product.dto.ProductFilterRequestDto;
import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.dto.ReviewDto;
import com.pyonsnalcolor.product.service.EventProductService;
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

@Tag(name = "이벤트 상품 api")
@RestController
@RequiredArgsConstructor
public class EventProductController {

    private final EventProductService eventProductService;

    @Operation(summary = "행사 상품 필터 조회", description = "행사 상품을 필터링 조회합니다.")
    @PostMapping("/products/event-products")
    public ResponseEntity<Page<EventProductResponseDto>> getEventProductsByFilter(
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam String storeType,
            @RequestBody ProductFilterRequestDto productFilterRequestDto
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ProductResponseDto> products = eventProductService.getPagedProductsDtoByFilter(
                pageable, storeType, productFilterRequestDto);
        return new ResponseEntity(products, HttpStatus.OK);
    }

    @Operation(summary = "리뷰 등록", description = "특정 상품의 리뷰를 등록합니다")
    @PostMapping(value = "/products/event-products/{id}/reviews",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> registerReview(
            @PathVariable String id,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestPart(value = "reviewDto") ReviewDto reviewDto
    ) throws Throwable {
        eventProductService.registerReview(imageFile, reviewDto, id);

        return ResponseEntity.noContent().build(); //현재는 리뷰 단건 조회하는 기능 존재 x -> location 지정 안함
    }

    @Operation(summary = "행사 상품 단건 조회", description = "id로 행사 상품을 조회합니다.")
    @GetMapping("/products/event-products/{id}")
    public ResponseEntity<EventProductResponseDto> getEventProduct(@PathVariable String id) {
        EventProductResponseDto responseDto = (EventProductResponseDto) eventProductService.getProductById(id);
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }
}
