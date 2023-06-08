package com.pyonsnalcolor.batch.service;

import com.pyonsnalcolor.batch.model.BasePbProduct;
import com.pyonsnalcolor.batch.model.StoreType;
import com.pyonsnalcolor.batch.repository.PbProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class PbBatchService implements BatchService {
    @Autowired
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

    private final List<BasePbProduct> getNewProducts(List<BasePbProduct> allProducts) {
        if(allProducts.isEmpty()) {
            return Collections.emptyList();
        }
        StoreType storeType = allProducts.get(0).getStoreType();
        List<BasePbProduct> alreadyExistProducts = pbProductRepository.findByStoreType(storeType);

        List<BasePbProduct> newProducts = allProducts.stream().filter(
                p -> !alreadyExistProducts.contains(p)
        ).collect(Collectors.toList());

        return newProducts;
    }

    protected abstract void sendAlarms(List<BasePbProduct> baseProducts);

    private final void saveProducts(List<BasePbProduct> baseProducts) {
        pbProductRepository.saveAll(baseProducts);
    }
}
