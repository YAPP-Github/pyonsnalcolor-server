package com.pyonsnalcolor.batch.service;

import com.pyonsnalcolor.product.enumtype.StoreType;
import com.pyonsnalcolor.promotion.entity.Promotion;
import com.pyonsnalcolor.promotion.repository.PromotionRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class PromotionBatchService implements BatchService {
    protected PromotionRepository promotionRepository;

    public PromotionBatchService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    public void execute() {
        List<Promotion> promotions = getAllPromotions();
        List<Promotion> newPromotions = getNewPromotions(promotions);
        List<Promotion> expiredPromotions = getExpiredPromotions(promotions);
        sendAlarms(newPromotions);
        savePromotions(newPromotions);
        deletePromotions(expiredPromotions);
    }

    private void deletePromotions(List<Promotion> expiredPromotions) {
        promotionRepository.deleteAll(expiredPromotions);
    }

    private void savePromotions(List<Promotion> newPromotions) {
        promotionRepository.saveAll(newPromotions);
    }

    // TODO : Alarm 서비스 완성 시 구현
    private void sendAlarms(List<Promotion> newPromotions) {
    }

    private List<Promotion> getExpiredPromotions(List<Promotion> promotions) {
        if(promotions.isEmpty()) {
            return Collections.emptyList();
        }
        StoreType storeType = promotions.get(0).getStoreType();

        List<Promotion> alreadyExistPromotions = promotionRepository.findByStoreType(storeType);
        List<Promotion> expiredPromotions = alreadyExistPromotions.stream().filter(
                p -> !promotions.contains(p)
        ).collect(Collectors.toList());

        return expiredPromotions;
    }

    private List<Promotion> getNewPromotions(List<Promotion> promotions) {
        if(promotions.isEmpty()) {
            return Collections.emptyList();
        }
        StoreType storeType = promotions.get(0).getStoreType();
        List<Promotion> alreadyExistPromotions = promotionRepository.findByStoreType(storeType);

        List<Promotion> newPromotions = promotions.stream().filter(
                p -> !alreadyExistPromotions.contains(p)
        ).collect(Collectors.toList());

        return newPromotions;
    }

    abstract public List<Promotion> getAllPromotions();

}
