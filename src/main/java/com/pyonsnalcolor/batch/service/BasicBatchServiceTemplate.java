package com.pyonsnalcolor.batch.service;

import com.pyonsnalcolor.product.entity.BaseProduct;
import com.pyonsnalcolor.product.repository.BasicProductRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@Slf4j
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
     *
     * @param allProducts
     * @return
     */
    protected List<T> getEventExpiredProducts(List<T> allProducts) {
        return Collections.emptyList();
    }

    protected <T extends BaseProduct> List<T> getNewProducts(List<T> allProducts) {
        return Collections.emptyList();
    }

    // TODO : Alarm 서비스 완성 시 구현
    private final void sendAlarms(List<T> baseProducts) {
    }

    private final void saveProducts(List<T> baseProducts) {
        basicProductRepository.saveAll(baseProducts);
    }

    /**
     * event 상품에 대해서만 구현체 작성 필요
     *
     * @param baseProducts
     */
    protected void deleteProducts(List<T> baseProducts) {
    }
}
