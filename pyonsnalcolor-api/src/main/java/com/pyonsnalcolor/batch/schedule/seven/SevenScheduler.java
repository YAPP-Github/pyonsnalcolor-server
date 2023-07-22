package com.pyonsnalcolor.batch.schedule.seven;

import com.pyonsnalcolor.batch.schedule.Scheduler;
import com.pyonsnalcolor.batch.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SevenScheduler extends Scheduler {
    @Autowired
    public SevenScheduler(@Qualifier("SevenPb") BatchService gs25PbBatchService,
                          @Qualifier("SevenEvent") BatchService gs25EventBatchService) {
        //TODO : 추가 필요
        super(gs25PbBatchService, gs25EventBatchService, null);
    }
}
