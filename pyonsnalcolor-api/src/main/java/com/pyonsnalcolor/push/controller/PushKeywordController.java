package com.pyonsnalcolor.push.controller;

import com.pyonsnalcolor.auth.AuthUserDetails;
import com.pyonsnalcolor.push.dto.PushKeywordRequestDto;
import com.pyonsnalcolor.push.dto.PushKeywordResponseDto;
import com.pyonsnalcolor.push.service.PushKeywordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "푸시 키워드 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/push/keyword")
public class PushKeywordController {

    private final PushKeywordService pushKeywordService;

    @Operation(summary = "푸시 키워드 목록 조회")
    @GetMapping
    public ResponseEntity<List<PushKeywordResponseDto>> getPushKeyword(
            @Parameter(hidden = true)
            @AuthenticationPrincipal AuthUserDetails authUserDetails
    ) {
        List<PushKeywordResponseDto> result = pushKeywordService.getPushKeywordResponseDto(authUserDetails);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @Operation(summary = "푸시 키워드 등록", description = "띄어쓰기, 특수문자없이 10자 이내")
    @PostMapping
    public ResponseEntity createPushKeyword(
            @Parameter(hidden = true)
            @AuthenticationPrincipal AuthUserDetails authUserDetails,
            @Valid @RequestBody PushKeywordRequestDto pushKeywordRequestDto
    ) {
        pushKeywordService.createPushKeyword(authUserDetails, pushKeywordRequestDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "푸시 키워드 삭제")
    @DeleteMapping("/{keywordId}")
    public ResponseEntity deletePushKeyword(
            @Parameter(hidden = true)
            @AuthenticationPrincipal AuthUserDetails authUserDetails,
            @PathVariable Long keywordId
    ) {
        pushKeywordService.deletePushKeyword(authUserDetails, keywordId);
        return new ResponseEntity(HttpStatus.OK);
    }
}