package com.pyonsnalcolor.batch.controller;

import com.pyonsnalcolor.batch.service.EventBatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "이벤트 상품 배치용 api", description = "관리자용 수동으로 크롤링하는 용도")
@RestController
public class EventBatchController {
    @Autowired
    private List<EventBatchService> batchServiceList;

    @Operation(summary = "이벤트 상품 업데이트", description = "지난 이벤트 상품 삭제하고 새 이벤트 상품 저장합니다.")
    @GetMapping("/manage/event-products/execute")
    public void executeEventBatch() {
        for (EventBatchService eventBatchService : batchServiceList) {
            eventBatchService.execute();
        }
    }
}
