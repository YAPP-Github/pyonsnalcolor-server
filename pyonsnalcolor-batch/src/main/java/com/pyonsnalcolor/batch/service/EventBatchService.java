package com.pyonsnalcolor.batch.service;

import com.pyonsnalcolor.batch.model.BaseEventProduct;
import com.pyonsnalcolor.batch.repository.EventProductRepository;

import java.util.List;

public abstract class EventBatchService implements BatchService {
    private EventProductRepository eventProductRepository;

    public EventBatchService(EventProductRepository eventProductRepository) {
        this.eventProductRepository = eventProductRepository;
    }

    @Override
    public void execute() {
        List<BaseEventProduct> allProducts = getAllProducts();
        List<BaseEventProduct> newProducts = getNewProducts(allProducts);
        List<BaseEventProduct> eventExpiredProducts = getEventExpiredProducts(allProducts);
        sendAlarms(newProducts);
        saveProducts(newProducts);
        deleteProducts(eventExpiredProducts);
    }

    protected abstract List<BaseEventProduct> getAllProducts();

    protected abstract List<BaseEventProduct> getEventExpiredProducts(List<BaseEventProduct> allProducts);

    protected abstract List<BaseEventProduct> getNewProducts(List<BaseEventProduct> allProducts);

    protected abstract void sendAlarms(List<BaseEventProduct> baseProducts);

    private final void saveProducts(List<BaseEventProduct> baseProducts) {
        eventProductRepository.saveAll(baseProducts);
    }

    private final void deleteProducts(List<BaseEventProduct> baseProducts) {
        eventProductRepository.deleteAll(baseProducts);
    }
}
