package com.pyonsnalcolor.product.repository;

import com.pyonsnalcolor.product.entity.BaseEventProduct;
import com.pyonsnalcolor.product.enumtype.StoreType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventProductRepository extends BasicProductRepository<BaseEventProduct, String> {
    Optional<BaseEventProduct> findByName(String name);
}
