package com.pyonsnalcolor.batch.service;

import com.pyonsnalcolor.product.entity.BaseEventProduct;
import com.pyonsnalcolor.product.entity.BaseProduct;
import com.pyonsnalcolor.product.enumtype.EventType;
import com.pyonsnalcolor.product.repository.EventProductRepository;
import com.pyonsnalcolor.product.repository.PbProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class EventBatchService extends BasicBatchServiceTemplate<BaseEventProduct> {

    @Autowired
    private PbProductRepository pbProductRepository;

    public EventBatchService(EventProductRepository eventProductRepository) {
        super(eventProductRepository);
    }

    @Override
    protected <T extends BaseProduct> List<T> getNewProducts(List<T> allProducts) {
        if (allProducts.isEmpty()) {
            return Collections.emptyList();
        }
        List<T> alreadyExistProducts = basicProductRepository.findAll();
        List<T> newProducts = allProducts.stream()
                .filter(product -> !alreadyExistProducts.contains(product))
                .peek(product -> log.info("새로운 행사 상품이 저장됩니다. {}", product))
                .collect(Collectors.toList());
        return newProducts;
    }

    @Override
    protected List<BaseEventProduct> getEventExpiredProducts(List<BaseEventProduct> allProducts) {
        if(allProducts.isEmpty()) {
            return Collections.emptyList();
        }

        List<BaseEventProduct> alreadyExistEventProducts = basicProductRepository.findAll();
        List<BaseEventProduct> expiredEventProducts = alreadyExistEventProducts.stream()
                .peek(product -> log.info("지난 행사 상품이 삭제됩니다. {}", product))
                .filter(product -> !alreadyExistEventProducts.contains(product))
                .collect(Collectors.toList());

        return expiredEventProducts;
    }

    @Override
    protected void deleteProducts(List<BaseEventProduct> expiredEventProducts) {
        updateEventTypeOfAllProductsIfExpired(expiredEventProducts);
        basicProductRepository.deleteAll(expiredEventProducts);
    }

    private void updateEventTypeOfAllProductsIfExpired(List<BaseEventProduct> expiredEventProducts) {
        expiredEventProducts
                .forEach(this::updateEventTypeIfEventExpired);
    }

    private void updateEventTypeIfEventExpired(BaseEventProduct baseEventProduct) {
        pbProductRepository.findAll().stream()
                .filter(basePbProduct -> basePbProduct.equals(baseEventProduct))
                .findFirst()
                .ifPresent(basePbProduct -> {
                    basePbProduct.updateEventType(EventType.NONE);
                    log.info("PB 상품 {}의 행사가 종료되어 행사 정보를 삭제합니다.", basePbProduct);
                });
    }
}