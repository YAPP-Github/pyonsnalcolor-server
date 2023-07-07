package com.pyonsnalcolor.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "관리자용 api")
@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminController {

    @Operation(summary = "EC2 헬스체크용")
    @GetMapping("/health-check")
    public ResponseEntity getHealthCheck() {
        return new ResponseEntity(HttpStatus.OK);
    }
}