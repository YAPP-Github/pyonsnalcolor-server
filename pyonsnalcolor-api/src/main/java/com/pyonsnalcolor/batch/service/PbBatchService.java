package com.pyonsnalcolor.batch.service;


import com.pyonsnalcolor.product.entity.BasePbProduct;
import com.pyonsnalcolor.product.repository.PbProductRepository;

public abstract class PbBatchService extends BasicBatchServiceTemplate<BasePbProduct> {
    public PbBatchService(PbProductRepository pbProductRepository) {
        super(pbProductRepository);
    }
}
