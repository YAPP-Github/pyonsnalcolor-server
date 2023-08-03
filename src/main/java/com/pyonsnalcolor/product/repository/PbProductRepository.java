package com.pyonsnalcolor.product.repository;

import com.pyonsnalcolor.product.entity.BasePbProduct;
import com.pyonsnalcolor.product.enumtype.Curation;
import com.pyonsnalcolor.product.enumtype.Recommend;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PbProductRepository extends BasicProductRepository<BasePbProduct, String> {

    List<BasePbProduct> findByRecommend(Recommend recommend);

    List<BasePbProduct> findByCuration(Curation curation);

}