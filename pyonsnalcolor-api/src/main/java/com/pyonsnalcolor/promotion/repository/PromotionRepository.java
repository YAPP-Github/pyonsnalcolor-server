package com.pyonsnalcolor.promotion.repository;

import com.pyonsnalcolor.promotion.entity.Promotion;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PromotionRepository extends MongoRepository<Promotion, String> {
}
