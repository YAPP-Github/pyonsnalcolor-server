package com.pyonsnalcolor.batch.repository;

import com.pyonsnalcolor.batch.model.BaseEventProduct;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventProductRepository extends MongoRepository<BaseEventProduct, Long> {
}
