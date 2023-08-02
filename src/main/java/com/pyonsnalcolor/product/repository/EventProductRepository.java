package com.pyonsnalcolor.product.repository;

import com.pyonsnalcolor.product.entity.BaseEventProduct;
import org.springframework.stereotype.Repository;

@Repository
public interface EventProductRepository extends BasicProductRepository<BaseEventProduct, String> {
}