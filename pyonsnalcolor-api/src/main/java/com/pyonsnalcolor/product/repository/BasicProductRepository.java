package com.pyonsnalcolor.product.repository;

import com.pyonsnalcolor.product.enumtype.StoreType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface BasicProductRepository<T, ID> extends MongoRepository<T, ID> {
    List<T> findByStoreType(StoreType storeType);

    Page<T> findByStoreType(StoreType storeType, Pageable pageable);

    List<T> findByNameContaining(String name);
}
