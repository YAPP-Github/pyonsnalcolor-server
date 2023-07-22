package com.pyonsnalcolor.batch.schedule.cu;

import com.pyonsnalcolor.batch.schedule.Scheduler;
import com.pyonsnalcolor.batch.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class CuScheduler extends Scheduler {
    @Autowired
    public CuScheduler(@Qualifier("CuPb") BatchService gs25PbBatchService,
                       @Qualifier("CuEvent") BatchService gs25EventBatchService,
                        @Qualifier("CuPromotion") BatchService cuPromotionBatchService) {
        super(gs25PbBatchService, gs25EventBatchService, cuPromotionBatchService);
    }
}
