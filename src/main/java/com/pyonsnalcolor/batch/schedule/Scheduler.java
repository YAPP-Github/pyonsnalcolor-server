package com.pyonsnalcolor.batch.schedule;

import com.pyonsnalcolor.batch.service.BatchService;
import com.pyonsnalcolor.batch.service.PromotionBatchService;
import com.pyonsnalcolor.promotion.entity.Promotion;
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
    public void run() {
        pbProductBatchService.execute();
        eventProductBatchService.execute();
        promotionBatchService.execute();
    }
}
