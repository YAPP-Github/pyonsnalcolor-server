package com.pyonsnalcolor.batch.service.gs25;

import com.pyonsnalcolor.batch.model.BasePbProduct;
import com.pyonsnalcolor.batch.repository.PbProductRepository;
import com.pyonsnalcolor.batch.service.PbBatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("GS25Pb")
@Slf4j
public class GS25PbBatchService extends PbBatchService {

    @Autowired
    public GS25PbBatchService(PbProductRepository pbProductRepository) {
        super(pbProductRepository);
    }

    @Override
    protected List<BasePbProduct> getNewProducts() {
        System.out.println("get new gs25 pb products");
        return null;
    }

    @Override
    protected void sendAlarms(List<BasePbProduct> gs25Products) {
        System.out.println("send gs25 pb products alarms");
    }
}
