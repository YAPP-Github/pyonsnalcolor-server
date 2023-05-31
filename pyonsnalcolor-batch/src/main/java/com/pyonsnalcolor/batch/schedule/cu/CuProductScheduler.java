package com.pyonsnalcolor.batch.schedule.cu;

import com.pyonsnalcolor.batch.schedule.Scheduler;
import com.pyonsnalcolor.batch.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class CuProductScheduler extends Scheduler {
    @Autowired
    public CuProductScheduler(@Qualifier("CuPb") BatchService gs25PbBatchService,
                              @Qualifier("CuEvent") BatchService gs25EventBatchService) {
        super(gs25PbBatchService, gs25EventBatchService);
    }
}
