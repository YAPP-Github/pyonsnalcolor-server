package com.pyonsnalcolor.batch.service;

import com.pyonsnalcolor.product.enumtype.StoreType;
import com.pyonsnalcolor.promotion.entity.Promotion;
import com.pyonsnalcolor.promotion.repository.PromotionRepository;

import java.util.List;
import java.util.Objects;

public abstract class PromotionBatchService implements BatchService {
    protected PromotionRepository promotionRepository;

    public PromotionBatchService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    public void execute() {
        List<Promotion> newPromotions = getNewPromotions();

        StoreType storeType = newPromotions.get(0).getStoreType();
        deleteExpiredPromotions(storeType);

        savePromotions(newPromotions);
    }

    abstract protected List<Promotion> getNewPromotions();

    private void deleteExpiredPromotions(StoreType storeType) {
        promotionRepository.deleteByStoreType(storeType);
    }

    private void savePromotions(List<Promotion> newPromotions) {
        promotionRepository.saveAll(newPromotions);
    }

    protected Promotion validateAllFieldsNotNull(Promotion promotion) {
        try {
            Objects.requireNonNull(promotion.getImage());
            Objects.requireNonNull(promotion.getThumbnailImage());
            Objects.requireNonNull(promotion.getTitle());
            return promotion;
        } catch (NullPointerException exception) {
            return null;
        }
    }
}