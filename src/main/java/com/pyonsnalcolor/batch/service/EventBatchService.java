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
                .peek(product -> log.info("새로운 행사 상품이 저장됩니다. {}", product.getName()))
                .collect(Collectors.toList());
        return newProducts;
    }

    @Override
    protected List<BaseEventProduct> updateExpiredProducts(List<BaseEventProduct> allProducts) {
        if(allProducts.isEmpty()) {
            return Collections.emptyList();
        }

        List<BaseEventProduct> alreadyExistEventProducts = basicProductRepository.findAll();
        List<BaseEventProduct> expiredEventProducts = alreadyExistEventProducts.stream()
                .filter(product -> !alreadyExistEventProducts.contains(product))
                .collect(Collectors.toList());

        updateEventTypeOfEventProductsIfExpired(expiredEventProducts);
        updateEventTypeOfPbProductsIfExpired(expiredEventProducts);

        return expiredEventProducts;
    }

    protected void updateEventTypeOfEventProductsIfExpired(List<BaseEventProduct> expiredEventProducts) {
        expiredEventProducts.stream()
                .forEach(e -> {
                            e.setIsEventExpiredTrue();
                            e.updateEventType(EventType.NONE);
                            basicProductRepository.save(e);
                        }
                );
    }

    private void updateEventTypeOfPbProductsIfExpired(List<BaseEventProduct> expiredEventProducts) {
        expiredEventProducts.forEach(baseEventProduct -> {
            pbProductRepository.findAll().stream()
                    .filter(basePbProduct -> basePbProduct.equals(baseEventProduct))
                    .findFirst()
                    .ifPresent(basePbProduct -> {
                        log.info("PB 상품 {}의 행사가 종료되어 행사 정보를 삭제합니다.", basePbProduct.getName());
                        basePbProduct.updateEventType(EventType.NONE);
                        pbProductRepository.save(basePbProduct);
                    });
        });
    }
}