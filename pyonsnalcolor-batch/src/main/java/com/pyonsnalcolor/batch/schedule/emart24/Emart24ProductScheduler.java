package com.pyonsnalcolor.batch.schedule.emart24;

import com.pyonsnalcolor.batch.schedule.Scheduler;
import com.pyonsnalcolor.batch.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Emart24ProductScheduler extends Scheduler {
    @Autowired
    public Emart24ProductScheduler(@Qualifier("Emart24Pb") BatchService gs25PbBatchService,
                                @Qualifier("Emart24Event") BatchService gs25EventBatchService) {
        super(gs25PbBatchService, gs25EventBatchService);
    }
}
