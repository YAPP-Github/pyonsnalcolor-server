package com.pyonsnalcolor.batch.repository;

import com.pyonsnalcolor.batch.model.BaseProduct;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository {
    public void saveAll(List<BaseProduct> gs25Products) {
        System.out.println("save products");
    }

    public void deleteAll(List<BaseProduct> gs25Products) {
        System.out.println("delete products");
    }
}
