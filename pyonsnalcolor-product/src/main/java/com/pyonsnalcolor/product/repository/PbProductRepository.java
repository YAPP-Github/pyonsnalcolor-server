package com.pyonsnalcolor.product.repository;


import com.pyonsnalcolor.product.model.BasePbProduct;
import org.springframework.stereotype.Repository;

@Repository
public interface PbProductRepository extends BasicProductRepository<BasePbProduct, String> {
}
