package com.pyonsnalcolor.push.controller;

import com.pyonsnalcolor.auth.AuthMemberId;
import com.pyonsnalcolor.push.dto.PushProductStoreRequestDto;
import com.pyonsnalcolor.push.dto.PushProductStoreResponseDto;
import com.pyonsnalcolor.push.service.PushProductStoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "푸시 구독 목록 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/push/stores")
public class PushProductStoreController {

    private final PushProductStoreService pushProductStoreService;

    @Operation(summary = "구독 목록 조회", description = "편의점/상품별 구독 현황을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<PushProductStoreResponseDto>> getPushProductStores(
            @Parameter(hidden = true) @AuthMemberId Long memberId
    ) {
        List<PushProductStoreResponseDto> result = pushProductStoreService.getPushProductStores(memberId);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @Operation(summary = "구독 신청", description = "편의점/상품별 구독을 신청합니다.")
    @PatchMapping("/subscribe")
    public ResponseEntity subscribePushProductStores(
            @Parameter(hidden = true) @AuthMemberId Long memberId,
            @RequestBody PushProductStoreRequestDto pushProductStoreRequestDto
    ) {
        pushProductStoreService.subscribePushProductStores(memberId, pushProductStoreRequestDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "구독 취소", description = "편의점/상품별 구독을 취소합니다.")
    @DeleteMapping("/unsubscribe")
    public ResponseEntity unsubscribePushProductStores(
            @Parameter(hidden = true) @AuthMemberId Long memberId,
            @RequestBody PushProductStoreRequestDto pushProductStoreRequestDto
    ) {
        pushProductStoreService.unsubscribePushProductStores(memberId, pushProductStoreRequestDto);
        return new ResponseEntity(HttpStatus.OK);
    }
}