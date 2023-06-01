package com.pyonsnalcolor.batch.service.emart24;

import com.pyonsnalcolor.batch.model.BaseEventProduct;
import com.pyonsnalcolor.batch.repository.EventProductRepository;
import com.pyonsnalcolor.batch.service.EventBatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("Emart24Event")
@Slf4j
public class Emart24EventBatchService extends EventBatchService {
    @Autowired
    public Emart24EventBatchService(EventProductRepository eventProductRepository) {
        super(eventProductRepository);
    }

    @Override
    protected List<BaseEventProduct> getAllProducts() {
        System.out.println("get expired emart24 event products");
        return null;
    }

    @Override
    protected List<BaseEventProduct> getEventExpiredProducts(List<BaseEventProduct> allProducts) {
        System.out.println("get expired emart24 event products");
        return null;
    }

    @Override
    protected List<BaseEventProduct> getNewProducts(List<BaseEventProduct> allProducts) {
        System.out.println("get new event emart24 products");
        return null;
    }

    @Override
    protected void sendAlarms(List<BaseEventProduct> emart24Products) {
        System.out.println("send event emart24 products alarms");
    }
}
