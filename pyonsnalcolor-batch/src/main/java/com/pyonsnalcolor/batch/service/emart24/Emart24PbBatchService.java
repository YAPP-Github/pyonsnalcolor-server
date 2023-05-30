package com.pyonsnalcolor.batch.service.emart24;

import com.pyonsnalcolor.batch.model.BaseProduct;
import com.pyonsnalcolor.batch.repository.ProductRepository;
import com.pyonsnalcolor.batch.service.PbBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("Emart24Pb")
public class Emart24PbBatchService extends PbBatchService {

    @Autowired
    public Emart24PbBatchService(ProductRepository productRepository) {
        super(productRepository);
    }

    @Override
    protected List<BaseProduct> getNewProducts() {
        System.out.println("get new emart24 pb products");
        return null;
    }

    @Override
    protected void sendAlarms(List<BaseProduct> emart24Products) {
        System.out.println("send emart24 pb products alarms");
    }
}
