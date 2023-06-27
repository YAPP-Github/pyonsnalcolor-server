package com.pyonsnalcolor.product.repository;


import com.pyonsnalcolor.product.model.BaseEventProduct;
import org.springframework.stereotype.Repository;

@Repository
public interface EventProductRepository extends BasicProductRepository<BaseEventProduct, String> {
}
