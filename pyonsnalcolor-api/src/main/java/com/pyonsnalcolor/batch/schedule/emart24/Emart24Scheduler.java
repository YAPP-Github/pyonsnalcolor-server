package com.pyonsnalcolor.batch.schedule.emart24;

import com.pyonsnalcolor.batch.schedule.Scheduler;
import com.pyonsnalcolor.batch.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


@Component
public class Emart24Scheduler extends Scheduler {
    @Autowired
    public Emart24Scheduler(@Qualifier("Emart24Pb") BatchService emart24PbBatchService,
                            @Qualifier("Emart24Event") BatchService emart24EventBatchService,
                            @Qualifier("Emart24Promotion") BatchService emart24PromotionBatchService) {
        super(emart24PbBatchService, emart24EventBatchService, emart24PromotionBatchService);
    }
}
