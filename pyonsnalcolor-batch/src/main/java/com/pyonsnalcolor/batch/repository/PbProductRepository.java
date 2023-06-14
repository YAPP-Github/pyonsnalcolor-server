package com.pyonsnalcolor.batch.repository;

import com.pyonsnalcolor.batch.model.BasePbProduct;
import org.springframework.stereotype.Repository;

@Repository
public interface PbProductRepository extends BasicProductRepository<BasePbProduct, String> {
}
