package com.pyonsnalcolor.batch.service.emart24;

import com.pyonsnalcolor.batch.model.BasePbProduct;
import com.pyonsnalcolor.batch.repository.PbProductRepository;
import com.pyonsnalcolor.batch.service.PbBatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("Emart24Pb")
@Slf4j
public class Emart24PbBatchService extends PbBatchService {

    @Autowired
    public Emart24PbBatchService(PbProductRepository pbProductRepository) {
        super(pbProductRepository);
    }

    @Override
    protected List<BasePbProduct> getNewProducts() {
        System.out.println("get new emart24 pb products");
        return null;
    }

    @Override
    protected void sendAlarms(List<BasePbProduct> emart24Products) {
        System.out.println("send emart24 pb products alarms");
    }
}
