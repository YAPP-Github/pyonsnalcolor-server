package com.pyonsnalcolor.batch.service;

import com.pyonsnalcolor.batch.model.BaseProduct;
import com.pyonsnalcolor.batch.repository.ProductRepository;

import java.util.List;

public abstract class PbBatchService implements BatchService {
    private ProductRepository productRepository;

    public PbBatchService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void execute() {
        List<BaseProduct> newProducts = getNewProducts();
        sendAlarms(newProducts);
        saveProducts(newProducts);
    }

    protected abstract List<BaseProduct> getNewProducts();

    protected abstract void sendAlarms(List<BaseProduct> baseProducts);

    private final void saveProducts(List<BaseProduct> baseProducts) {
        productRepository.saveAll(baseProducts);
    }
}
