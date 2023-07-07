package com.pyonsnalcolor.batch.controller;

import com.pyonsnalcolor.batch.service.PbBatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "PB 상품 배치용 api", description = "관리자용 수동으로 크롤링하는 용도")
@RestController
public class PbBatchController {
    @Autowired
    private List<PbBatchService> batchServiceList;

    @Operation(summary = "PB 상품 업데이트", description = "새 PB 상품 저장합니다.")
    @GetMapping("/manage/pb-products/execute")
    public void executePbBatch() {
        for (PbBatchService pbBatchService : batchServiceList) {
            pbBatchService.execute();
        }
    }
}
