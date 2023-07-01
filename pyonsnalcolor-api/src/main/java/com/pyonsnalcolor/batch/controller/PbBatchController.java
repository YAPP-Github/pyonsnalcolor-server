package com.pyonsnalcolor.batch.controller;

import com.pyonsnalcolor.batch.service.PbBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PbBatchController {
    @Autowired
    private List<PbBatchService> batchServiceList;


    @GetMapping("/manage/pb-products/execute")
    public void executePbBatch() {
        for (PbBatchService pbBatchService : batchServiceList) {
            pbBatchService.execute();
        }
    }
}
