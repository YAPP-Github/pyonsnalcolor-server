package com.pyonsnalcolor.batch.service.gs25;

import com.pyonsnalcolor.batch.model.BaseProduct;
import com.pyonsnalcolor.batch.repository.ProductRepository;
import com.pyonsnalcolor.batch.service.EventBatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * TODO : 편의점에 맞게 구현 필요
 */
@Service("GS25Event")
@Slf4j
public class GS25EventBatchService extends EventBatchService {
    @Autowired
    public GS25EventBatchService(ProductRepository productRepository) {
        super(productRepository);
    }

    @Override
    protected List<BaseProduct> getEventExpiredProducts() {
        System.out.println("get expired gs25 event products");
        return null;
    }

    @Override
    protected List<BaseProduct> getNewProducts() {
        System.out.println("get new event gs25 products");
        return null;
    }

    @Override
    protected void sendAlarms(List<BaseProduct> gs25Products) {
        System.out.println("send event gs25 products alarms");
    }
}
