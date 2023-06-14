package com.pyonsnalcolor.batch.service;

import com.pyonsnalcolor.batch.model.BaseEventProduct;
import com.pyonsnalcolor.batch.model.StoreType;
import com.pyonsnalcolor.batch.repository.EventProductRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class EventBatchService extends BasicBatchServiceTemplate<BaseEventProduct> {
    public EventBatchService(EventProductRepository eventProductRepository) {
        super(eventProductRepository);
    }

    @Override
    protected List<BaseEventProduct> getEventExpiredProducts(List<BaseEventProduct> allProducts) {
        if(allProducts.isEmpty()) {
            return Collections.emptyList();
        }
        StoreType storeType = allProducts.get(0).getStoreType();
        List<BaseEventProduct> alreadyExistEventProducts = basicProductRepository.findByStoreType(storeType);
        List<BaseEventProduct> expiredEventProducts = alreadyExistEventProducts.stream().filter(
                p -> !allProducts.contains(p)
        ).collect(Collectors.toList());

        return expiredEventProducts;
    }

    @Override
    protected void deleteProducts(List<BaseEventProduct> baseProducts) {
        basicProductRepository.deleteAll(baseProducts);
    }
}
