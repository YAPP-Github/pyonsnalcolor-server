package com.pyonsnalcolor.batch.repository;

import com.pyonsnalcolor.batch.model.BaseEventProduct;
import com.pyonsnalcolor.batch.model.BasePbProduct;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventProductRepository {
    public void saveAll(List<BaseEventProduct> gs25Products) {
        System.out.println("save products");
    }

    public void deleteAll(List<BaseEventProduct> gs25Products) {
        System.out.println("delete products");
    }
}
