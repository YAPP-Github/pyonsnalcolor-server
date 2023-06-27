package com.pyonsnalcolor.repository;


import com.pyonsnalcolor.model.StoreType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BasicProductRepository<T, ID> extends MongoRepository<T, ID> {
    Page<T> findByStoreType(StoreType storeType, Pageable pageable);
}
