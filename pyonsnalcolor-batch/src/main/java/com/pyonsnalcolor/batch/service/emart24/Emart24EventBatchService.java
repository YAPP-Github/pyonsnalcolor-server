package com.pyonsnalcolor.batch.service.emart24;

import com.pyonsnalcolor.batch.model.BaseProduct;
import com.pyonsnalcolor.batch.repository.ProductRepository;
import com.pyonsnalcolor.batch.service.EventBatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("Emart24Event")
@Slf4j
public class Emart24EventBatchService extends EventBatchService {
    @Autowired
    public Emart24EventBatchService(ProductRepository productRepository) {
        super(productRepository);
    }

    @Override
    protected List<BaseProduct> getEventExpiredProducts() {
        System.out.println("get expired emart24 event products");
        return null;
    }

    @Override
    protected List<BaseProduct> getNewProducts() {
        System.out.println("get new event emart24 products");
        return null;
    }

    @Override
    protected void sendAlarms(List<BaseProduct> emart24Products) {
        System.out.println("send event emart24 products alarms");
    }
}
