package com.pyonsnalcolor.schedule.seven;

import com.pyonsnalcolor.schedule.Scheduler;
import com.pyonsnalcolor.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SevenProductScheduler extends Scheduler {
    @Autowired
    public SevenProductScheduler(@Qualifier("SevenPb") BatchService gs25PbBatchService,
                                 @Qualifier("SevenEvent") BatchService gs25EventBatchService) {
        super(gs25PbBatchService, gs25EventBatchService);
    }
}
