package com.pyonsnalcolor.batch.service;

import com.pyonsnalcolor.batch.model.BaseProduct;
import com.pyonsnalcolor.batch.repository.ProductRepository;

import java.util.List;

public abstract class EventBatchService implements BatchService {
    private ProductRepository productRepository;

    public EventBatchService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void execute() {
        List<BaseProduct> newProducts = getNewProducts();
        List<BaseProduct> eventExpiredProducts = getEventExpiredProducts();
        sendAlarms(newProducts);
        saveProducts(eventExpiredProducts);
        deleteProducts(eventExpiredProducts);
    }

    protected abstract List<BaseProduct> getEventExpiredProducts();

    protected abstract List<BaseProduct> getNewProducts();

    protected abstract void sendAlarms(List<BaseProduct> baseProducts);

    private final void saveProducts(List<BaseProduct> baseProducts) {
        productRepository.saveAll(baseProducts);
    }

    private final void deleteProducts(List<BaseProduct> baseProducts) {
        productRepository.deleteAll(baseProducts);
    }
}
