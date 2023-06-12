package com.pyonsnalcolor.batch.service;

import com.pyonsnalcolor.batch.model.BaseEventProduct;
import com.pyonsnalcolor.batch.repository.EventProductRepository;

import java.util.List;

public abstract class EventBatchService extends BasicBatchServiceTemplate<BaseEventProduct> {
    public EventBatchService(EventProductRepository eventProductRepository) {
        super(eventProductRepository);
    }

    @Override
    protected void deleteProducts(List<BaseEventProduct> baseProducts) {
        basicProductRepository.deleteAll(baseProducts);
    }
}
