package com.pyonsnalcolor.admin;

import com.pyonsnalcolor.batch.service.EventBatchService;
import com.pyonsnalcolor.batch.service.PbBatchService;
import com.pyonsnalcolor.push.dto.DeviceTokenRequestDto;
import com.pyonsnalcolor.push.fcm.FcmPushService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Tag(name = "관리자용 api")
@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    private List<EventBatchService> eventBatchServices;

    @Autowired
    private List<PbBatchService> pbBatchServices;

    @Autowired
    private FcmPushService fcmPushService;

    // universial link
    @GetMapping(value = {"/apple-app-site-association", "/.well-known/apple-app-site-association"})
    public ResponseEntity<String> getAASAData() throws IOException {
        byte[] jsonData = Files.readAllBytes(Paths.get("/home/aasa/apple-app-site-association"));

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new String(jsonData));
    }

    // ios 테스트용
    @GetMapping("/fcm/test")
    public ResponseEntity fcmTest(DeviceTokenRequestDto deviceTokenRequestDto) throws ExecutionException, InterruptedException {
        fcmPushService.pbFcmTest(deviceTokenRequestDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "이벤트 상품 업데이트", description = "(관리자용) 지난 이벤트 상품 삭제하고 새 이벤트 상품 저장합니다.")
    @GetMapping("/manage/event-products/execute")
    public void executeEventBatch() {
        for (EventBatchService eventBatchService : eventBatchServices) {
            eventBatchService.execute();
        }
    }

    @Operation(summary = "PB 상품 업데이트", description = "(관리자용) 지난 PB 상품 삭제하고 새 PB 상품 저장합니다.")
    @GetMapping("/manage/pb-products/execute")
    public void executePbBatch() {
        for (PbBatchService pbBatchService : pbBatchServices) {
            pbBatchService.execute();
        }
    }

    @Operation(summary = "EC2 헬스체크용")
    @GetMapping("/health-check")
    public ResponseEntity getHealthCheck() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
