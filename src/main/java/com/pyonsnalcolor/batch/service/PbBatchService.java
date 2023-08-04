package com.pyonsnalcolor.batch.service;

import com.pyonsnalcolor.product.entity.BasePbProduct;
import com.pyonsnalcolor.product.entity.BaseProduct;
import com.pyonsnalcolor.product.repository.EventProductRepository;
import com.pyonsnalcolor.product.repository.PbProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class PbBatchService extends BasicBatchServiceTemplate<BasePbProduct> {

    @Autowired
    private EventProductRepository eventProductRepository;

    public PbBatchService(PbProductRepository pbProductRepository) {
        super(pbProductRepository);
    }

    @Override
    protected final <T extends BaseProduct> List<T> getNewProducts(List<T> allProducts) {
        if (allProducts.isEmpty()) {
            return Collections.emptyList();
        }
        updateEventTypeOfAllProducts(allProducts); // 신상만 찾기!! 해도 좋을 듯

        List<T> alreadyExistProducts = basicProductRepository.findAll();
        updateIsNewOfAlreadyExistProducts(alreadyExistProducts);

        List<T> newProducts = allProducts.stream()
                .filter(product -> !alreadyExistProducts.contains(product))
                .peek(product -> log.info("새로운 PB 상품이 저장됩니다. {}", product))
                .peek(product -> product.updateIsNew(true))
                .collect(Collectors.toList());
        return newProducts;
    }

    private <T extends BaseProduct> void updateIsNewOfAlreadyExistProducts(List<T> alreadyExistProducts) {
        alreadyExistProducts.stream()
                .filter(p -> p.getIsNew() != null && p.getIsNew())
                .forEach(product -> {
                    product.updateIsNew(false);
                    log.info("지난 주 PB 상품이 신상품에서 제외됩니다. {}", product);
                });
    }

    private <T extends BaseProduct> void updateEventTypeOfAllProducts(List<T> alreadyExistProducts) {
        alreadyExistProducts
                .forEach(this::updateEventTypeIfEventInProgress);
    }

    private void updateEventTypeIfEventInProgress(BaseProduct baseProduct) {
        eventProductRepository.findAll().stream()
                .filter(baseEventProduct -> baseEventProduct.equals(baseProduct))
                .findFirst()
                .ifPresent(matchingEventProduct -> {
                    baseProduct.updateEventType(matchingEventProduct.getEventType());
                    log.info("PB 상품이 현재 {} 행사 진행 중입니다. {}", matchingEventProduct.getEventType(), baseProduct);
                });
    }
}