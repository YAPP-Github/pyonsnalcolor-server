package com.pyonsnalcolor.batch.repository;

import com.pyonsnalcolor.batch.model.BasePbProduct;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PbProductRepository {
    public void saveAll(List<BasePbProduct> gs25Products) {
        System.out.println("save products");
    }

    public void deleteAll(List<BasePbProduct> gs25Products) {
        System.out.println("delete products");
    }
}
