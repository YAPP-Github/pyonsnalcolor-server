package com.pyonsnalcolor.batch.schedule.seven;

import com.pyonsnalcolor.batch.schedule.Scheduler;
import com.pyonsnalcolor.batch.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SevenScheduler extends Scheduler {
    @Autowired
    public SevenScheduler(@Qualifier("SevenPb") BatchService sevenPbBatchService,
                          @Qualifier("SevenEvent") BatchService sevenEventBatchService,
                          @Qualifier("SevenPromotion") BatchService sevenPromotionBatchService) {
        super(sevenPbBatchService, sevenEventBatchService, sevenPromotionBatchService);
    }
}
