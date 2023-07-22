package com.pyonsnalcolor.promotion.repository;

import com.pyonsnalcolor.product.enumtype.StoreType;
import com.pyonsnalcolor.promotion.entity.Promotion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PromotionRepository extends MongoRepository<Promotion, String> {
    List<Promotion> findByStoreType(StoreType storeType);

    void deleteByStoreType(StoreType storeType);
}
