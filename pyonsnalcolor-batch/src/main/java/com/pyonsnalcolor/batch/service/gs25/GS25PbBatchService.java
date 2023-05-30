package com.pyonsnalcolor.batch.service.gs25;

import com.pyonsnalcolor.batch.model.BaseProduct;
import com.pyonsnalcolor.batch.repository.ProductRepository;
import com.pyonsnalcolor.batch.service.PbBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO : 편의점에 맞게 구현 필요
 */
@Service("GS25Pb")
public class GS25PbBatchService extends PbBatchService {

    @Autowired
    public GS25PbBatchService(ProductRepository productRepository) {
        super(productRepository);
    }

    @Override
    protected List<BaseProduct> getNewProducts() {
        System.out.println("get new gs25 pb products");
        return null;
    }

    @Override
    protected void sendAlarms(List<BaseProduct> gs25Products) {
        System.out.println("send gs25 pb products alarms");
    }
}
