package com.pyonsnalcolor.repository;

import com.pyonsnalcolor.model.BasePbProduct;
import org.springframework.stereotype.Repository;

@Repository
public interface PbProductRepository extends BasicProductRepository<BasePbProduct, String> {
}
