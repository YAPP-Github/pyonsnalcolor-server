package com.pyonsnalcolor.batch.repository;

import com.pyonsnalcolor.batch.model.BasePbProduct;
import com.pyonsnalcolor.batch.model.StoreType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PbProductRepository extends MongoRepository<BasePbProduct, Long> {
    List<BasePbProduct> findByStoreType(StoreType storeType);
}
