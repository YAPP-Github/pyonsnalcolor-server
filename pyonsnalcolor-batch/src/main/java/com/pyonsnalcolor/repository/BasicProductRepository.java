package com.pyonsnalcolor.repository;

import com.pyonsnalcolor.model.StoreType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface BasicProductRepository<T, ID> extends MongoRepository<T, ID> {
    List<T> findByStoreType(StoreType storeType);
}
