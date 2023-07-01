package com.pyonsnalcolor.batch.controller;

import com.pyonsnalcolor.batch.service.EventBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EventBatchController {
    @Autowired
    private List<EventBatchService> batchServiceList;

    @GetMapping("/manage/event-products/execute")
    public void executeEventBatch() {
        for (EventBatchService eventBatchService : batchServiceList) {
            eventBatchService.execute();
        }
    }
}
