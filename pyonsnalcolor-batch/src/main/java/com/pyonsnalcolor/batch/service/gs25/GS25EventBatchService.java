package com.pyonsnalcolor.batch.service.gs25;

import com.pyonsnalcolor.batch.model.BaseEventProduct;
import com.pyonsnalcolor.batch.repository.EventProductRepository;
import com.pyonsnalcolor.batch.service.EventBatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("GS25Event")
@Slf4j
public class GS25EventBatchService extends EventBatchService {
    @Autowired
    public GS25EventBatchService(EventProductRepository eventProductRepository) {
        super(eventProductRepository);
    }

    @Override
    protected List<BaseEventProduct> getAllProducts() {
        System.out.println("get all gs25 event products");
        return null;
    }

    @Override
    protected List<BaseEventProduct> getEventExpiredProducts(List<BaseEventProduct> allProducts) {
        System.out.println("get expired gs25 event products");
        return null;
    }

    @Override
    protected List<BaseEventProduct> getNewProducts(List<BaseEventProduct> allProducts) {
        System.out.println("get new event gs25 products");
        return null;
    }

    @Override
    protected void sendAlarms(List<BaseEventProduct> gs25Products) {
        System.out.println("send event gs25 products alarms");
    }
}
