package com.pyonsnalcolor.batch.service;

import com.pyonsnalcolor.batch.model.BaseEventProduct;
import com.pyonsnalcolor.batch.model.BasePbProduct;
import com.pyonsnalcolor.batch.repository.PbProductRepository;

import java.util.List;

public abstract class PbBatchService implements BatchService {
    private PbProductRepository pbProductRepository;

    public PbBatchService(PbProductRepository pbProductRepository) {
        this.pbProductRepository = pbProductRepository;
    }

    @Override
    public void execute() {
        List<BasePbProduct> allProducts = getAllProducts();
        List<BasePbProduct> newProducts = getNewProducts(allProducts);
        sendAlarms(newProducts);
        saveProducts(newProducts);
    }

    protected abstract List<BasePbProduct> getAllProducts();

    protected abstract List<BasePbProduct> getNewProducts(List<BasePbProduct> allProducts);

    protected abstract void sendAlarms(List<BasePbProduct> baseProducts);

    private final void saveProducts(List<BasePbProduct> baseProducts) {
        pbProductRepository.saveAll(baseProducts);
    }
}
