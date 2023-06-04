package com.pyonsnalcolor.batch.service.seven;

import com.pyonsnalcolor.batch.model.BasePbProduct;
import com.pyonsnalcolor.batch.repository.PbProductRepository;
import com.pyonsnalcolor.batch.service.PbBatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("SevenPb")
@Slf4j
public class SevenPbBatchService extends PbBatchService {

    @Autowired
    public SevenPbBatchService(PbProductRepository pbProductRepository) {
        super(pbProductRepository);
    }

    @Override
    protected List<BasePbProduct> getAllProducts() {
        return null;
    }

    @Override
    protected List<BasePbProduct> getNewProducts(List<BasePbProduct> allProducts) {
        return null;
    }

    @Override
    protected void sendAlarms(List<BasePbProduct> SevenProducts) {
        System.out.println("send Seven pb products alarms");
    }
}
