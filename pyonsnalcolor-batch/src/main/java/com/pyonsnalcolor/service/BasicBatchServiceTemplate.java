package com.pyonsnalcolor.service;

import com.pyonsnalcolor.model.BaseProduct;
import com.pyonsnalcolor.model.StoreType;
import com.pyonsnalcolor.repository.BasicProductRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

abstract class BasicBatchServiceTemplate<T extends BaseProduct> implements BatchService {
    protected BasicProductRepository basicProductRepository;

    public BasicBatchServiceTemplate(BasicProductRepository basicProductRepository) {
        this.basicProductRepository = basicProductRepository;
    }

    @Override
    public void execute() {
        List<T> allProducts = getAllProducts();
        List<T> newProducts = getNewProducts(allProducts);
        List<T> eventExpiredProducts = getEventExpiredProducts(allProducts);
        sendAlarms(newProducts);
        saveProducts(newProducts);
        deleteProducts(eventExpiredProducts);
    }

    protected abstract List<T> getAllProducts();

    /**
     * event 상품에 대해서만 구현체 작성 필요
     * @param allProducts
     * @return
     */
    protected List<T> getEventExpiredProducts(List<T> allProducts) {
        return Collections.emptyList();
    }

    private final List<T> getNewProducts(List<T> allProducts) {
        if(allProducts.isEmpty()) {
            return Collections.emptyList();
        }
        StoreType storeType = allProducts.get(0).getStoreType();
        List<T> alreadyExistProducts = basicProductRepository.findByStoreType(storeType);

        List<T> newProducts = allProducts.stream().filter(
                p -> !alreadyExistProducts.contains(p)
        ).collect(Collectors.toList());

        return newProducts;
    }

    // TODO : Alarm 서비스 완성 시 구현
    private final void sendAlarms(List<T> baseProducts) {}

    private final void saveProducts(List<T> baseProducts) {
        basicProductRepository.saveAll(baseProducts);
    }

    /**
     * event 상품에 대해서만 구현체 작성 필요
     * @param baseProducts
     */
    protected void deleteProducts(List<T> baseProducts) {}
}
