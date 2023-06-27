package com.pyonsnalcolor.repository;


import com.pyonsnalcolor.model.BaseEventProduct;
import org.springframework.stereotype.Repository;

@Repository
public interface EventProductRepository extends BasicProductRepository<BaseEventProduct, String> {
}
