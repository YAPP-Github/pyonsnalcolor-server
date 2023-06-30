package com.pyonsnalcolor.batch.schedule;

import com.pyonsnalcolor.batch.service.BatchService;

public abstract class Scheduler {
    private BatchService pbProductBatchService;
    private BatchService eventProductBatchService;

    public Scheduler(BatchService pbProductBatchService, BatchService eventProductBatchService) {
        this.pbProductBatchService = pbProductBatchService;
        this.eventProductBatchService = eventProductBatchService;
    }

    //@Scheduled(fixedRate = 10000)
    public void run() {
        pbProductBatchService.execute();
        eventProductBatchService.execute();
    }
}
