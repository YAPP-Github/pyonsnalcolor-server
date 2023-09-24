package com.pyonsnalcolor.member.controller;

import com.pyonsnalcolor.member.AuthMemberId;
import com.pyonsnalcolor.member.dto.FavoriteRequestDto;
import com.pyonsnalcolor.member.service.MemberService;
import com.pyonsnalcolor.product.ProductFactory;
import com.pyonsnalcolor.product.dto.ProductResponseDto;
import com.pyonsnalcolor.product.enumtype.ProductType;
import com.pyonsnalcolor.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "찜하기 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/favorites")
public class FavoriteController {

    private final MemberService memberService;
    private final ProductFactory productFactory;

    @Operation(summary = "찜한 상품 조회", description = "찜한 PB 혹은 행사 상품을 조회합니다.")
    @GetMapping
    public ResponseEntity<Slice<ProductResponseDto>> getFavorites(
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam ProductType productType,
            @Parameter(hidden = true) @AuthMemberId Long memberId
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Slice<String> productIds = memberService.getProductIdsOfFavorite(pageable, memberId, productType);
        ProductService productService = productFactory.getService(productType);
        Slice<ProductResponseDto> results = productService.getProductsOfFavoriteByIds(productIds);
        return new ResponseEntity(results, HttpStatus.OK);
    }

    @Operation(summary = "찜하기 등록", description = "상품을 찜하기에 등록합니다.")
    @PostMapping
    public ResponseEntity<Void> createFavorite(
            @Parameter(hidden = true) @AuthMemberId Long memberId,
            @RequestBody FavoriteRequestDto favoriteRequestDto
    ) {
        memberService.createFavorite(memberId, favoriteRequestDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "찜하기 취소", description = "상품을 찜하기에서 취소합니다.")
    @DeleteMapping
    public ResponseEntity<Void> deleteFavorite(
            @Parameter(hidden = true) @AuthMemberId Long memberId,
            @RequestBody FavoriteRequestDto favoriteRequestDto
    ) {
        memberService.deleteFavorite(memberId, favoriteRequestDto);
        return new ResponseEntity(HttpStatus.OK);
    }
}