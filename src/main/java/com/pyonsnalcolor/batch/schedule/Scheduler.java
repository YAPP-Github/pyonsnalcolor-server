package com.pyonsnalcolor.batch.schedule;

import com.pyonsnalcolor.batch.service.BatchService;
import org.springframework.scheduling.annotation.Scheduled;

public abstract class Scheduler {
    private BatchService pbProductBatchService;
    private BatchService eventProductBatchService;
    private BatchService promotionBatchService;

    public Scheduler(BatchService pbProductBatchService,
                     BatchService eventProductBatchService,
                     BatchService promotionBatchService) {
        this.pbProductBatchService = pbProductBatchService;
        this.eventProductBatchService = eventProductBatchService;
        this.promotionBatchService = promotionBatchService;
    }

    @Scheduled(cron = "0 0 6 * * SUN")
    public void runProductBatch() {
        pbProductBatchService.execute();
        eventProductBatchService.execute();
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void runPromotionBatch() {
        promotionBatchService.execute();
    }
}
